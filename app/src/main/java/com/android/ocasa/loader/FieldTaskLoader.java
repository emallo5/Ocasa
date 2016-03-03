package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.model.History;

import java.util.List;


/**
 * Created by ignacio on 11/02/16.
 */
public class FieldTaskLoader extends AsyncTaskLoader<List<History>> {

    private long recordId;
    private String columnId;

    private List<History> historical;

    public FieldTaskLoader(Context context, long recordId, String columnId) {
        super(context);
        this.recordId = recordId;
        this.columnId = columnId;
    }

    @Override
    public List<History> loadInBackground() {

        return new HistoryDAO(getContext()).findHistoricalForColumnAndRecord(columnId, recordId);
    }

    @Override
    public void deliverResult(List<History> data) {
        this.historical = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(historical != null)
            deliverResult(historical);
        else{
            forceLoad();
        }
    }
}
