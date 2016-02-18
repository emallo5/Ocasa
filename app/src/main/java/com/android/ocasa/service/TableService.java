package com.android.ocasa.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.http.service.HttpService;
import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.ocasa.sync.SyncService;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ignacio on 26/01/16.
 */
public class TableService {

    public static final String DOWNLOAD_TABLE_REQUEST = "com.android.ocasa.service.DOWNLOAD_TABLE_REQUEST";

    static final String TABLE_URL = "/android/table/1/column/";

    static final String TABLE_PATH = "table";
    static final String COLUMN_PATH = "column";

    private Context context;

    public TableService(Context context){
        this.context = context;
    }

    public void syncTable(String tableId, RequestCallback<HttpTable> callback){
        HttpService service = HttpService.getInstance(context);

        Gson gson = new GsonBuilder().registerTypeAdapter(Column.class, new HttpTable.ColumnDeserializer()).create();

        service.newGetRequest(buildColumnUrl(tableId), HttpTable.class, gson, callback);
    }

    private String buildColumnUrl(String tableId){

        Uri builtUri = Uri.parse(BuildConfig.BASE_URL)
                .buildUpon()
                .appendPath("android")
                .appendPath(TABLE_PATH)
                .appendPath(tableId)
                .appendPath(COLUMN_PATH)
                .appendPath(COLUMN_PATH + tableId + ".json").build();

        return builtUri.toString();
    }

    public static class SaveTableResponseCallback extends GenericRequestCallback<HttpTable> {

        private Table table;

        public SaveTableResponseCallback(Context context, String tableId) {
            super(context);
            this.table = new Table();
            this.table.setId(tableId);
        }

        @Override
        public void onSuccess(HttpTable response) {
            super.onSuccess(response);

            saveTable(response);
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);
        }

        private void saveTable(HttpTable httpTable){

            TableDAO tableDAO = new TableDAO(getContext());
            ColumnDAO dao = new ColumnDAO(getContext());

            for (Column column : httpTable.getColumns()){
                column.setTable(table);

                if(column.getFieldType() == FieldType.COMBO){
                    tableDAO.save(column.getRelationship());

                    Intent intent = new Intent(DOWNLOAD_TABLE_REQUEST);
                    intent.putExtra("id", column.getRelationship().getId());

                    getContext().sendBroadcast(intent);
                }
            }

            dao.save(httpTable.getColumns());
        }
    }


}
