package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.ReceiptItemDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
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
public class ReceiptTaskLoaderTest extends AsyncTaskLoader<FormViewModel> {

    private long receiptId;

    public ReceiptTaskLoaderTest(Context context, long receiptId) {
        super(context);
        this.receiptId = receiptId;
    }

    @Override
    public FormViewModel loadInBackground() {

        FormViewModel form = new FormViewModel();

        Receipt receipt = new ReceiptDAO(getContext()).findById(receiptId);

        receipt.setItems(new ReceiptItemDAO(getContext()).findForReceipt(receipt.getId()));

        List<ColumnAction> columnHeaders = new ColumnActionDAO(getContext()).
                findColumnsForActionAndType(receipt.getAction().getId(), ColumnAction.ColumnActionType.HEADER);

        for (ColumnAction columnAction : columnHeaders){

            FieldViewModel field = new FieldViewModel();
            field.setLabel(columnAction.getColumn().getName());
            field.setType(columnAction.getColumn().getFieldType());
            field.setValue(columnAction.getDefaultValue());
            field.setTag(String.valueOf(columnAction.getColumn().getId()));

            if(columnAction.getColumn().getFieldType() == FieldType.COMBO){

                Column column = columnAction.getColumn();

                List<Column> relationship = new ColumnDAO(getContext()).findLogicColumnsForTable(column.getRelationship().getId());

                List<FieldViewModel> fields = new ArrayList<>();

                String value = new ArrayList<>(receipt.getItems()).get(0).getRecord().getFieldForColumn(column.getId()).getValue();

                Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(column.getRelationship().getId(), value);

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

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
