package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 21/03/16.
 */
public class RecordsTaskLoader extends AsyncTaskLoader<List<Record>> {

    private List<Record> data;
    private long[] recordIds;

    public RecordsTaskLoader(Context context, long[] recordIds) {
        super(context);
        this.recordIds = recordIds;
    }

    @Override
    public List<Record> loadInBackground() {

        List<Record> records = new ArrayList<>();

        RecordDAO dao = new RecordDAO(getContext());

        ReceiptCacheManager cacheManager = ReceiptCacheManager.getInstance();

        if(recordIds == null)
            return records;

        for (long recordId: recordIds) {
            Record record = dao.findById(recordId);

            if(!cacheManager.recordExists(recordId)) {
                cacheManager.saveRecord(record);
            }

            cacheManager.fillRecord(record);
            records.add(record);
        }

        return records;
    }

    @Override
    public void deliverResult(List<Record> data) {
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
