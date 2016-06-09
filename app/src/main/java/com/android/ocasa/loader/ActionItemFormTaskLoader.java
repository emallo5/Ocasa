package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.service.RecordService;
import com.android.ocasa.viewmodel.FormViewModel;

/**
 * Ignacio Oviedo on 14/04/16.
 */
public class ActionItemFormTaskLoader extends AsyncTaskLoader<FormViewModel> {

    private long recordId;
    private String actionId;

    private FormViewModel record;

    public ActionItemFormTaskLoader(Context context, long recordId, String actionId) {
        super(context);
        this.recordId = recordId;
        this.actionId = actionId;
    }

    @Override
    public FormViewModel loadInBackground() {

        RecordService service = new RecordService(getContext());

        return service.getFormFromRecordAndAction(recordId, actionId);
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
