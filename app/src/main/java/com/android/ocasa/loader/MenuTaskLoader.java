package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.model.Application;

import java.util.List;

/**
 * Created by ignacio on 14/01/16.
 */
public class MenuTaskLoader extends AsyncTaskLoader<List<Application>> {

    private List<Application> menu;

    public MenuTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Application> loadInBackground() {

        ApplicationDAO applicationDAO = new ApplicationDAO(getContext());

        return applicationDAO.findAll();
    }

    @Override
    public void deliverResult(List<Application> data) {
        this.menu = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(menu != null)
            deliverResult(menu);
        else{
            forceLoad();
        }
    }
}
