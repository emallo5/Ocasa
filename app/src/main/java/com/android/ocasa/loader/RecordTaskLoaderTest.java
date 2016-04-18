package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 14/04/16.
 */
public class RecordTaskLoaderTest extends AsyncTaskLoader<FormViewModel> {

    private long recordId;

    private FormViewModel record;

    public RecordTaskLoaderTest(Context context, long recordId) {
        super(context);
        this.recordId = recordId;
    }

    @Override
    public FormViewModel loadInBackground() {

        FormViewModel form = new FormViewModel();

        RecordDAO recordDAO = new RecordDAO(getContext());

        Record record = recordDAO.findById(recordId);

        form.setTitle(record.getTable().getName());
        form.setId(record.getId());

        for (Field field : record.getFields()){

            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());

            Column column = field.getColumn();

            fieldViewModel.setTag(column.getId());
            fieldViewModel.setLabel(column.getName());
            fieldViewModel.setType(column.getFieldType());

            if(field.getColumn().getFieldType() == FieldType.COMBO){

                fieldViewModel.setRelationshipTable(field.getColumn().getRelationship().getId());

                List<FieldViewModel> relationship = new ArrayList<>();

                Column primaryColumn = new ColumnDAO(getContext()).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getId());

                Record comboRecord = recordDAO.findForColumnAndValue(primaryColumn.getId(), field.getValue());

                for (Field comboField : comboRecord.getLogicFields()){

                    FieldViewModel comboViewModel = new FieldViewModel();
                    comboViewModel.setValue(comboField.getValue());

                    Column comboColumn = comboField.getColumn();

                    comboViewModel.setTag(comboColumn.getId());
                    comboViewModel.setLabel(comboColumn.getName());
                    comboViewModel.setType(comboColumn.getFieldType());

                    relationship.add(comboViewModel);
                }

                fieldViewModel.setRelationshipFields(relationship);
            }

            form.addField(fieldViewModel);
        }

        return form;
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
