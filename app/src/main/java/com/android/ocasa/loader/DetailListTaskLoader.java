package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.cache.dao.FieldDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.model.Record;

import java.util.List;

/**
 * Created by ignacio on 25/02/16.
 */
public class DetailListTaskLoader extends AsyncTaskLoader<List<Record>> {

    private String tableId;
    private String columnId;
    private String value;

    private List<Record> table;

    public DetailListTaskLoader(Context context, String tableId, String columnId, String value) {
        super(context);
        this.tableId = tableId;
        this.columnId = columnId;
        this.value = value;
    }

    @Override
    public List<Record> loadInBackground() {

        RecordDAO recordDAO = new RecordDAO(getContext());

        List<Record> records = recordDAO.findDetailRecords(tableId, columnId, value);

        for (Record record : records){
            record.setFields(new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(record.getId())));
        }

        return records;
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
