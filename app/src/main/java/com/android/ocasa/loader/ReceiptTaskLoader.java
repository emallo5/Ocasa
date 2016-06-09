package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.ReceiptItemDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;

import java.util.ArrayList;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class ReceiptTaskLoader extends AsyncTaskLoader<Receipt> {

    private Receipt data;

    private long receiptId;

    public ReceiptTaskLoader(Context context, long receiptId) {
        super(context);
        this.receiptId = receiptId;
    }

    @Override
    public Receipt loadInBackground() {

        Receipt receipt = new ReceiptDAO(getContext()).findById(receiptId);

        Action action = receipt.getAction();

        action.setColumnsHeader(new ColumnActionDAO(getContext()).
                findColumnsForActionAndType(action.getId(), ColumnAction.ColumnActionType.HEADER));

        receipt.setItems(new ReceiptItemDAO(getContext()).findForReceipt(receiptId));

        for (ColumnAction columnAction : action.getColumnsHeader()){
            if(columnAction.getColumn().getFieldType() == FieldType.COMBO){

                Column column = columnAction.getColumn();

                Field field = new ArrayList<>(receipt.getItems()).get(0).getRecord().getFieldForColumn(column.getId());

                Column primaryColumn = new ColumnDAO(getContext()).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getId());

                Record relationship = RecordDAO.getInstance(getContext()).findForColumnAndValue(primaryColumn.getId(), field.getValue());
                relationship.setFields(
                        new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(relationship.getId())));

            }
        }

        return receipt;
    }

    @Override
    public void deliverResult(Receipt data) {
        this.data = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(data != null)
            deliverResult(data);
        else{
            forceLoad();
        }
    }
}
