package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordTaskLoader extends AsyncTaskLoader<Record> {

    private long recordId;

    private Record record;

    public RecordTaskLoader(Context context, long recordId) {
        super(context);
        this.recordId = recordId;
    }

    @Override
    public Record loadInBackground() {

        RecordDAO recordDAO = new RecordDAO(getContext());

        Record record = recordDAO.findById(recordId);

        for (Field field : record.getFields()){
            if(field.getColumn().getFieldType() == FieldType.COMBO){

                Column primaryColumn = new ColumnDAO(getContext()).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getId());

                Record relationship = recordDAO.findForColumnAndValue(primaryColumn.getId(), field.getValue());
                relationship.setFields(relationship.getLogicFields());

                field.setRelationship(relationship);
            }
        }

        return record;
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
