package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.model.Field;


/**
 * Created by ignacio on 11/02/16.
 */
public class FieldTaskLoader extends AsyncTaskLoader<Field> {

    private int fieldId;

    private Field field;

    public FieldTaskLoader(Context context, int fieldId) {
        super(context);
        this.fieldId = fieldId;
    }

    @Override
    public Field loadInBackground() {

        Field field = new FieldDAO(getContext()).finById(fieldId);
        field.setHistorical(new HistoryDAO(getContext()).findHistoricalForField(String.valueOf(field.getId())));

        return field;
    }

    @Override
    public void deliverResult(Field data) {
        this.field = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(field != null)
            deliverResult(field);
        else{
            forceLoad();
        }
    }
}
