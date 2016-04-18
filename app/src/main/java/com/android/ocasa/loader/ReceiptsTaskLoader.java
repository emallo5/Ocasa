package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.model.Receipt;

import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class ReceiptsTaskLoader extends AsyncTaskLoader<List<Receipt>> {

    private List<Receipt> data;

    private String actionId;

    public ReceiptsTaskLoader(Context context, String actionId) {
        super(context);
        this.actionId = actionId;
    }

    @Override
    public List<Receipt> loadInBackground() {
        return new ReceiptDAO(getContext()).findForAction(actionId);
    }

    @Override
    public void deliverResult(List<Receipt> data) {
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
