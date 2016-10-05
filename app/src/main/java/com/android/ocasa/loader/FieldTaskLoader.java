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

import java.util.List;


/**
 * Created by ignacio on 11/02/16.
 */
public class FieldTaskLoader extends AsyncTaskLoader<List<History>> {

    private long recordId;
    private String columnId;

    private List<History> historical;

    public FieldTaskLoader(Context context, long recordId, String columnId) {
        super(context);
        this.recordId = recordId;
        this.columnId = columnId;
    }

    @Override
    public List<History> loadInBackground() {

        List<History> histories = new HistoryDAO(getContext()).findHistoricalForColumnAndRecord(columnId, recordId);

        ColumnDAO columnDAO = new ColumnDAO(getContext());

        Column column = columnDAO.findById(columnId);

        for (int index = 0; index < histories.size(); index++){
            History history = histories.get(index);

            if(column.getFieldType() == FieldType.COMBO){
                fillComboValue(column, history);
            }
        }

        return histories;
    }

    private void fillComboValue(Column column, History history){
        Column primaryColumn = new ColumnDAO(getContext()).findPrimaryKeyColumnForTable(column.getRelationship().getExternalID(),
                column.getRelationship().getTable().getId());

        Record record = RecordDAO.getInstance(getContext()).findForColumnAndValue(primaryColumn.getId(), history.getValue());

        if(record == null)
            return;

        List<Field> fields = record.getLogicFields();

        StringBuilder value = new StringBuilder();

        for (int index = 0; index < fields.size(); index++){

            Field comboField = fields.get(index);

            if(value.length() > 0){
                value.append("\n");
            }

            value.append(comboField.getValue());
        }

        history.setValue(value.toString());
    }

    @Override
    public void deliverResult(List<History> data) {
        this.historical = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(historical != null)
            deliverResult(historical);
        else{
            forceLoad();
        }
    }
}
