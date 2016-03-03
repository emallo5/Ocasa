package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class TableTaskLoader extends AsyncTaskLoader<Table> {

    private String tableId;
    private String query;

    private Table table;

    public TableTaskLoader(Context context, String tableId, String query) {
        super(context);
        this.tableId = tableId;
        this.query = query;
    }

    @Override
    public Table loadInBackground() {

        TableDAO tableDAO = new TableDAO(getContext());

        Table table = tableDAO.finById(tableId);

        if(query != null)
            table.setRecords(new RecordDAO(getContext()).findRecordsForTableAndQuery(tableId, query));
        else
            table.setRecords(new RecordDAO(getContext()).findRecordsForTable(tableId));

        return table;
    }

    private List<Record> filter(List<Record> records){

        List<Record> filterRecords = new ArrayList<>();

        filterRecords.addAll(applySubFilter(records, "2", "Test"));

        List<Record> aux = applySubFilter(filterRecords, "4", "test");

        filterRecords.clear();
        filterRecords.addAll(aux);

        aux = applySubFilter(filterRecords, "7", "23:45");

        filterRecords.clear();
        filterRecords.addAll(aux);

        return filterRecords;
    }

    private List<Record> applySubFilter(List<Record> records, String columnId, String value){

        List<Record> subList = new ArrayList<>();

        for (Record record : records){

            Field field = record.getFieldForColumn(columnId);

            if(field.getValue().contains(value)){
                subList.add(record);
            }
        }

        return subList;
    }

    @Override
    public void deliverResult(Table data) {
        this.table = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(table != null)
            deliverResult(table);
        else{
            forceLoad();
        }
    }
}
