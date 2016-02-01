package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.http.service.HttpService;
import com.android.ocasa.httpmodel.Table;
import com.android.ocasa.model.Column;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ignacio on 26/01/16.
 */
public class TableService {

    static final String TABLE_URL = "/android/table/1/column/";

    private Context context;

    public TableService(Context context){
        this.context = context;
    }

    public void syncTable(RequestCallback<Table> callback){
        HttpService service = HttpService.getInstance(context);

        Gson gson = new GsonBuilder().registerTypeAdapter(Column.class, new Table.ColumnDeserializer()).create();

        service.newGetRequest(BuildConfig.BASE_URL + TABLE_URL, Table.class, gson, callback);
    }

    public static class SaveTableResponseCallback extends GenericRequestCallback<Table> {

        public SaveTableResponseCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Table response) {
            super.onSuccess(response);

            saveTable(response);
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);

        }

        private void saveTable(Table table){

            ColumnDAO dao = new ColumnDAO(getContext());
            dao.save(table.getColumns());
        }
    }



}
