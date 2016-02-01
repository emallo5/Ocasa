package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;

import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordTaskLoader extends AsyncTaskLoader<Record> {

    private int recordId;

    private Record record;

    public RecordTaskLoader(Context context, int recordId) {
        super(context);
        this.recordId = recordId;
    }


    @Override
    public Record loadInBackground() {

        RecordDAO recordDAO = new RecordDAO(getContext());

        return recordDAO.finById(recordId);
    }

    @Override
    public void deliverResult(Record data) {
        this.record = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(record != null)
            deliverResult(record);
        else{
            forceLoad();
        }
    }
}
