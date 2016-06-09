package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class ReceiptsTaskLoader extends AsyncTaskLoader<List<CellViewModel>> {

    private List<CellViewModel> data;

    private String actionId;

    public ReceiptsTaskLoader(Context context, String actionId) {
        super(context);
        this.actionId = actionId;
    }

    @Override
    public List<CellViewModel> loadInBackground() {

        List<CellViewModel> cells = new ArrayList<>();

        List<Receipt> receipts = new ReceiptDAO(getContext()).findForAction(actionId);

        for (Receipt receipt : receipts) {

            CellViewModel cell = new CellViewModel();
            cell.setId(receipt.getId());

            List<FieldViewModel> fieldViewModels = new ArrayList<>();

            for (Field headerField : receipt.getHeaderValues()) {

                FieldViewModel field = new FieldViewModel();
                field.setValue(headerField.getValue());
                field.setLabel(headerField.getColumn().getName());
                field.setType(headerField.getColumn().getFieldType());
                field.setTag(String.valueOf(headerField.getColumn().getId()));

                if (headerField.getColumn().getFieldType() == FieldType.COMBO) {

                    field.setRelationshipTable(headerField.getColumn().getRelationship().getId());

                    Column column = headerField.getColumn();

                    List<Column> relationship = new ColumnDAO(getContext()).findLogicColumnsForTable(column.getRelationship().getId());

                    Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(column.getRelationship().getId(), headerField.getValue());

                    if (record != null)
                        record.setFields(new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(record.getId())));

                    for (Column col : relationship) {

//                        FieldViewModel subField = new FieldViewModel();
//                        subField.setLabel(col.getName());
//                        subField.setType(col.getFieldType());
//                        subField.setTag(String.valueOf(col.getId()));

                        if (record != null)
                            field.setValue(record.getFieldForColumn(col.getId()).getValue());

//                        fields.add(subField);
                    }

//                    field.setRelationshipFields(fields);

                }

                fieldViewModels.add(field);
            }

            cell.setFields(fieldViewModels);
            cells.add(cell);
        }

        return cells;
    }

    @Override
    public void deliverResult(List<CellViewModel> data) {
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
