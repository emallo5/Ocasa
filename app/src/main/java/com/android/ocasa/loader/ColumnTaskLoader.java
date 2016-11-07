package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.cache.dao.ColumnDAO;
import com.android.ocasa.model.Column;

import java.util.List;

/**
 * Created by ignacio on 15/02/16.
 */
public class ColumnTaskLoader extends AsyncTaskLoader<List<Column>> {

    private String tableId;

    private List<Column> data;

    public ColumnTaskLoader(Context context, String tableId) {
        super(context);
        this.tableId = tableId;
    }

    @Override
    public List<Column> loadInBackground() {
        return new ColumnDAO(getContext()).findColumnsForTable(tableId);
    }

    @Override
    public void deliverResult(List<Column> data) {
        this.data = data;

        if(isStarted())
            super.deliverResult(data);
    }

   @Override
    protected void onStartLoading() {
        if(data != null)
            deliverResult(data);
        else{
            forceLoad();
        }
    }
}
