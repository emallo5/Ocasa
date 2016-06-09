package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 09/05/16.
 */
public class EditReceiptTaskLoader extends AsyncTaskLoader<FormViewModel> {

    private String actionId;

    private FormViewModel form;

    public EditReceiptTaskLoader(Context context, String actionId) {
        super(context);
        this.actionId = actionId;
    }

    @Override
    public FormViewModel loadInBackground() {

        FormViewModel form = new FormViewModel();

        Action action = new ActionDAO(getContext()).findById(actionId);
        form.setTitle(action.getName());

        ColumnActionDAO columnActionDAO = new ColumnActionDAO(getContext());

        List<ColumnAction> columnsHeader = columnActionDAO.
                findColumnsForActionAndType(actionId, ColumnAction.ColumnActionType.HEADER);

        for (ColumnAction columnAction : columnsHeader){

            FieldViewModel field = new FieldViewModel();
            field.setLabel(columnAction.getColumn().getName());
            field.setType(columnAction.getColumn().getFieldType());
            field.setValue(columnAction.getDefaultValue());
            field.setTag(String.valueOf(columnAction.getColumn().getId()));
            field.setEditable(columnAction.isEditable());

            if(columnAction.getColumn().getFieldType() == FieldType.COMBO){

                field.setRelationshipTable(columnAction.getColumn().getRelationship().getId());

                Column column = columnAction.getColumn();

                List<Column> relationship = new ColumnDAO(getContext()).findLogicColumnsForTable(column.getRelationship().getId());

                List<FieldViewModel> fields = new ArrayList<>();

                Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(column.getRelationship().getId(), columnAction.getDefaultValue());

                if(record != null)
                    record.setFields(new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(record.getId())));

                for (Column col : relationship){

                    FieldViewModel subField = new FieldViewModel();
                    subField.setLabel(col.getName());
                    subField.setType(col.getFieldType());
                    subField.setTag(String.valueOf(col.getId()));

                    if(record != null)
                        subField.setValue(record.getFieldForColumn(col.getId()).getValue());

                    fields.add(subField);
                }

                field.setRelationshipFields(fields);
            }

            form.addField(field);
        }

        return form;
    }

    @Override
    public void deliverResult(FormViewModel data) {
        form = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {

        if(form != null)
            deliverResult(form);
        else
            forceLoad();
    }
}
