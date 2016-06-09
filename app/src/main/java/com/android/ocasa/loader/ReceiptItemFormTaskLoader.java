package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.service.RecordService;
import com.android.ocasa.viewmodel.FormViewModel;

/**
 * Ignacio Oviedo on 14/04/16.
 */
public class ReceiptItemFormTaskLoader extends AsyncTaskLoader<FormViewModel> {

    private long recordId;
    private long receiptId;

    private FormViewModel record;

    public ReceiptItemFormTaskLoader(Context context, long recordId, long receiptId) {
        super(context);
        this.recordId = recordId;
        this.receiptId = receiptId;
    }

    @Override
    public FormViewModel loadInBackground() {

        RecordService service = new RecordService(getContext());

        return service.getFormFromRecordAndReceipt(recordId, receiptId);
    }

    @Override
    public void deliverResult(FormViewModel data) {
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
