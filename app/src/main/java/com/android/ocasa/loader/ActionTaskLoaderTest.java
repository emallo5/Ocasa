package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 04/04/16.
 */
public class ActionTaskLoaderTest extends AsyncTaskLoader<FormViewModel> {

    private long actionId;

    public ActionTaskLoaderTest(Context context, long actionId) {
        super(context);
        this.actionId = actionId;
    }

    @Override
    public FormViewModel loadInBackground() {

        FormViewModel form = new FormViewModel();

        Receipt receipt = new ReceiptDAO(getContext()).findById(actionId);
        form.setTitle(receipt.getAction().getName());

        form.setId(receipt.getId());

        List<Field> headers = new FieldDAO(getContext()).findForReceipt(actionId);

        FieldViewModel number = new FieldViewModel();
        number.setLabel("Numero comprobante");
        number.setValue(String.valueOf(receipt.getNumber()));

        //form.addField(number);

        for (Field header : headers){

            FieldViewModel field = new FieldViewModel();
            field.setTag(header.getColumn().getId());
            field.setLabel(header.getColumn().getName());
            field.setValue(header.getValue());


            if(header.getColumn().getFieldType() == FieldType.COMBO){
//
                Column column = header.getColumn();
//
                List<Column> relationship = new ColumnDAO(getContext()).findLogicColumnsForTable(column.getRelationship().getId());
//
                List<FieldViewModel> fields = new ArrayList<>();
//
                Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(column.getRelationship().getId(), header.getValue());
//
                if(record != null)
                    record.setFields(new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(record.getId())));

                for (Column col : relationship){

//                    FieldViewModel subField = new FieldViewModel();
//                    subField.setLabel(col.getName());
//                    subField.setType(col.getFieldType());
//                    subField.setTag(String.valueOf(col.getId()));

                    if(record != null) {
                        //subField.setValue(record.getFieldForColumn(col.getId()).getValue());
                        field.setValue(record.getFieldForColumn(col.getId()).getValue());
                    }

//                    fields.add(subField);
                }

                field.setRelationshipFields(fields);
            }
//
            form.addField(field);
        }

        return form;
    }

    @Override
    public void deliverResult(FormViewModel data) {

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
