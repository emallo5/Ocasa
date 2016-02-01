package com.android.ocasa.service;

import android.content.Context;
import android.net.Uri;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.OptionDAO;
import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.http.service.HttpService;
import com.android.ocasa.httpmodel.Table;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Option;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ignacio on 26/01/16.
 */
public class TableService {

    static final String TABLE_URL = "/android/table/1/column/";

    static final String TABLE_PATH = "table";
    static final String COLUMN_PATH = "column";

    private Context context;

    public TableService(Context context){
        this.context = context;
    }

    public void syncTable(String tableId, RequestCallback<Table> callback){
        HttpService service = HttpService.getInstance(context);

        Gson gson = new GsonBuilder().registerTypeAdapter(Column.class, new Table.ColumnDeserializer()).create();

        service.newGetRequest(buildColumnUrl(tableId), Table.class, gson, callback);
    }

    private String buildColumnUrl(String tableId){

        Uri builtUri = Uri.parse(BuildConfig.BASE_URL)
                .buildUpon()
                .appendPath("android")
                .appendPath(TABLE_PATH)
                .appendPath(tableId)
                .appendPath(COLUMN_PATH).build();

        return builtUri.toString();
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

            for (Column column : table.getColumns()){

                if(column.getOptions() != null) {
                    for (Option option : column.getOptions()) {
                        option.setColumn(column);
                    }

                    OptionDAO optionDao = new OptionDAO(getContext());
                    optionDao.save(column.getOptions());
                }
            }
        }
    }


}
