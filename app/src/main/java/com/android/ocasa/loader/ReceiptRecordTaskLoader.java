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

import java.util.ArrayList;

/**
 * Created by Emiliano Mallo on 31/03/16.
 */
public class ReceiptRecordTaskLoader extends AsyncTaskLoader<Record> {

    private long recordId;
    private ArrayList<String> availableColumns;

    private Record record;

    public ReceiptRecordTaskLoader(Context context, long recordId, ArrayList<String> availableColumns) {
        super(context);
        this.recordId = recordId;
        this.availableColumns = availableColumns;
    }

    @Override
    public Record loadInBackground() {
        RecordDAO recordDAO = new RecordDAO(getContext());

        Record record = recordDAO.findById(recordId);

        record.setFields(new FieldDAO(getContext()).findForAvailableColumns(recordId, availableColumns));

        for (Field field : record.getFields()){
            if(field.getColumn().getFieldType() == FieldType.COMBO){

                Column primaryColumn = new ColumnDAO(getContext()).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getId());

                Record relationship = recordDAO.findForColumnAndValue(primaryColumn.getId(), field.getValue());
                relationship.setFields(
                        new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(relationship.getId())));

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
