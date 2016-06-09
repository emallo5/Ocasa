package com.android.ocasa.pickup.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FormViewModel;

/**
 * Created by Emiliano Mallo on 28/04/16.
 */
public class PickupFormTaskLoader extends AsyncTaskLoader<FormViewModel> {

    private long recordId;

    private FormViewModel form;

    public PickupFormTaskLoader(Context context, long recordId) {
        super(context);
        this.recordId = recordId;
    }

    @Override
    public FormViewModel loadInBackground() {

        FormViewModel form = new FormViewModel();

        Record record = RecordDAO.getInstance(getContext()).findById(recordId);

        form.setId(record.getId());
        form.setTitle(record.getLogicFields().get(0).getValue());

        return form;
    }

    @Override
    public void deliverResult(FormViewModel data) {
        this.form = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(form != null)
            deliverResult(form);
        else{
            forceLoad();
        }
    }
}
