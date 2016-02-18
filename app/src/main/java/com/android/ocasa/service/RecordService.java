package com.android.ocasa.service;

import android.content.Context;
import android.net.Uri;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.http.service.HttpService;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordService {

    public static final String RECORD_SYNC_FINISHED_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_FINISHED_ACTION";
    public static final String RECORD_SYNC_ERROR_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_ERROR_ACTION";

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

    public void saveRecord(Record record){
        HistoryDAO historyDAO = new HistoryDAO(context);

        for (Field field : record.getFields()){
            historyDAO.save(field.getHistorical());
        }

        FieldDAO dao = new FieldDAO(context);
        dao.update(record.getFields());
    }

    private String buildRecordUrl(String tableId){

        Uri builtUri = Uri.parse(BuildConfig.BASE_URL)
                .buildUpon()
                .appendPath("android")
                .appendPath(TABLE_PATH)
                .appendPath(tableId)
                .appendPath(RECORD_PATH)
                .appendPath(RECORD_PATH + tableId + ".json").build();

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

            NotificationManager.sendBroadcast(getContext(), RECORD_SYNC_FINISHED_ACTION);

        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);

            NotificationManager.sendBroadcast(getContext(), RECORD_SYNC_ERROR_ACTION);
        }

        private void saveTableRecord(TableRecord tableRecord){

            RecordDAO recordDAO = new RecordDAO(getContext());
            recordDAO.deleteRecordsForTable(table.getId());

            FieldDAO dao = new FieldDAO(getContext());

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
