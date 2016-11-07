package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.cache.dao.ColumnDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 17/10/16.
 */

public class FieldService {

    public FieldViewModel getFieldFromField(Context context, Field field){

        FieldViewModel fieldViewModel = convertField(field);

        if(field.isComboField()){
            fieldViewModel.setRelationshipTable(field.getColumn().getRelationship().getExternalID());
            fieldViewModel.setRelationshipFields(getComboFields(context, field));
        }

        return fieldViewModel;
    }

    public FieldViewModel getFieldFromColumn(Context context, Column column){

        FieldViewModel fieldViewModel = convertColumn(column);

        if(column.isCombo()){
            fieldViewModel.setRelationshipTable(column.getRelationship().getExternalID());
            fieldViewModel.setRelationshipFields(getComboColumns(context, column));
        }

        return fieldViewModel;
    }

    private List<FieldViewModel> getComboFields(Context context, Field field){
        List<FieldViewModel> relationship = new ArrayList<>();

        Column primaryColumn = new ColumnDAO(context).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getExternalID(),
                field.getColumn().getRelationship().getTable().getId());

        Record record = new RecordDAO(context).findForColumnAndValue(primaryColumn.getId(), field.getValue());

        if(record == null)
            return relationship;

        List<Field> fields = record.getLogicFields();

        for (int index = 0; index < fields.size(); index++){

            Field comboField = fields.get(index);

            relationship.add(convertField(comboField));
        }

        return relationship;
    }

    private List<FieldViewModel> getComboColumns(Context context, Column column){
        List<FieldViewModel> relationship = new ArrayList<>();

        List<Column> columns = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getExternalID());

        for (int index = 0; index < columns.size(); index++){

            Column comboColumn= columns.get(index);

            relationship.add(convertColumn(comboColumn));
        }

        return relationship;
    }

    private FieldViewModel convertField(Field field){
        FieldViewModel fieldViewModel = new FieldViewModel();
        fieldViewModel.setValue(field.getValue());

        Column column = field.getColumn();

        fieldViewModel.setTag(column.getId());
        fieldViewModel.setLabel(column.getName());
        fieldViewModel.setType(column.getFieldType());
        fieldViewModel.setEditable(column.isEditable());

        return fieldViewModel;
    }

    private FieldViewModel convertColumn(Column column){
        FieldViewModel fieldViewModel = new FieldViewModel();

        fieldViewModel.setValue("");
        fieldViewModel.setTag(column.getId());
        fieldViewModel.setLabel(column.getName());
        fieldViewModel.setType(column.getFieldType());
        fieldViewModel.setEditable(true);

        return fieldViewModel;
    }


}
