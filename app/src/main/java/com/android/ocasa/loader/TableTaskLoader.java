package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Record;

import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class TableTaskLoader extends AsyncTaskLoader<List<Record>> {

    private String tableId;

    private List<Record> table;

    public TableTaskLoader(Context context, String tableId) {
        super(context);
        this.tableId = tableId;
    }

    @Override
    public List<Record> loadInBackground() {

        RecordDAO recordDAO = new RecordDAO(getContext());

        return recordDAO.findRecordsForTable(tableId);
    }

    @Override
    public void deliverResult(List<Record> data) {
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
