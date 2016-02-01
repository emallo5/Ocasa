package com.android.ocasa.service;

import android.content.Context;
import android.net.Uri;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.http.service.HttpService;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordService {

    static final String TABLE_PATH = "table";
    static final String RECORD_PATH = "record";

    private Context context;

    public RecordService(Context context){
        this.context = context;
    }

    public void syncRecord(String tableId, GenericRequestCallback<TableRecord> callback){

        HttpService service = HttpService.getInstance(context);

        Gson gson = new GsonBuilder().registerTypeAdapter(Field.class, new TableRecord.FieldDeserializer()).create();

        service.newGetRequest(buildRecordUrl(tableId), TableRecord.class, gson, callback);
    }

    private String buildRecordUrl(String tableId){

        Uri builtUri = Uri.parse(BuildConfig.BASE_URL)
                .buildUpon()
                .appendPath("android")
                .appendPath(TABLE_PATH)
                .appendPath(tableId)
                .appendPath(RECORD_PATH).build();

        return builtUri.toString();
    }

    public static class SaveRecordResponseCallback extends GenericRequestCallback<TableRecord> {

        private Table table;

        public SaveRecordResponseCallback(Context context, String tableId) {
            super(context);
            this.table = new Table();
            this.table.setId(tableId);
        }

        @Override
        public void onSuccess(TableRecord response) {
            super.onSuccess(response);

            saveTableRecord(response);

            NotificationManager.sendBroadcast(getContext(), UserService.USER_LOGIN_FINISHED_ACTION);

        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);

            NotificationManager.sendBroadcast(getContext(), UserService.USER_LOGIN_FINISHED_ACTION);
        }

        private void saveTableRecord(TableRecord tableRecord){

            RecordDAO recordDAO = new RecordDAO(getContext());
            recordDAO.deleteAll();

            FieldDAO dao =  new FieldDAO(getContext());
            dao.deleteAll();

            for (Record record : tableRecord.getRecords()){
                record.setTable(table);

                recordDAO.save(record);

                for (Field field : record.getFields()){
                    field.setRecord(record);
                }

                dao.save(record.getFields());
            }
        }
    }

}
