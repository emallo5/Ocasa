package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class DetailReceiptRecordTaskLoader extends AsyncTaskLoader<Record> {

    private long recordId;
    private long receiptId;

    private Record record;

    public DetailReceiptRecordTaskLoader(Context context, long recordId, long receiptId) {
        super(context);
        this.recordId = recordId;
        this.receiptId = receiptId;
    }

    @Override
    public Record loadInBackground() {
        RecordDAO recordDAO = new RecordDAO(getContext());
        HistoryDAO historyDAO = new HistoryDAO(getContext());
        FieldDAO fieldDao = new FieldDAO(getContext());

        Record record = recordDAO.findById(recordId);
        record.setFields(fieldDao.findFieldsForRecord(String.valueOf(record.getId())));

        for (Field field : record.getFields()){
            History history = historyDAO.findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));

            if(history != null && history.getReceipt().getId() == receiptId){
                field.setValue(history.getValue());
            }

            if(field.getColumn().getFieldType() == FieldType.COMBO){

                Column primaryColumn = new ColumnDAO(getContext()).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getId());

                Record relationship = recordDAO.findForColumnAndValue(primaryColumn.getId(), field.getValue());
                relationship.setFields(fieldDao.findLogicsForRecord(String.valueOf(relationship.getId())));

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
