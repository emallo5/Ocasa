package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;

import java.util.List;

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

        Record record = recordDAO.finById(recordId);

        for (Field field : record.getFields()){
            if(field.getColumn().getFieldType() == FieldType.COMBO){

                String[] values = field.getValue().split(",");

                Record relationship = recordDAO.findRecordsForColumnAndValue(values[0], values[1]);
                relationship.setFields(
                        new FieldDAO(getContext()).findFieldsLogicForRecord(String.valueOf(relationship.getId())));

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
