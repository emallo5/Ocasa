package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.ReceiptItemDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 14/07/16.
 */
public class ReceiptService {

    public ReceiptService(){

    }

    public ReceiptFormViewModel getHeaderByReceipt(Context context, long receiptId){
        ReceiptFormViewModel form = new ReceiptFormViewModel();

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);
        form.setTitle(receipt.getAction().getName());
        form.setOpen(receipt.isOpen());


        Category category = new CategoryDAO(context).findById(receipt.getAction().getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        form.setColor(application.getReceiptColor());
        form.setId(receipt.getId());

        List<Field> headers = new FieldDAO(context).findForReceipt(receiptId);

        for (Field header : headers){

            FieldViewModel field = new FieldViewModel();
            field.setTag(header.getColumn().getId());
            field.setLabel(header.getColumn().getName());
            field.setValue(header.getValue());


            if(header.getColumn().getFieldType() == FieldType.COMBO){

                Column column = header.getColumn();

                List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getId());

                List<FieldViewModel> fields = new ArrayList<>();

                Record record = RecordDAO.getInstance(context).findForTableAndValueId(column.getRelationship().getId(), header.getValue());

                if(record != null)
                    record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

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

    public TableViewModel getItemsByReceipt(Context context, long receiptId){
        TableViewModel table = new TableViewModel();

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        table.setName(receipt.getAction().getTable().getName());

        ReceiptItemDAO dao = new ReceiptItemDAO(context);

        List<ReceiptItem> records = dao.findForReceipt(receiptId);

        for (ReceiptItem item: records) {
            Record record = item.getRecord();

            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());

            fillCell(context, receiptId, cell);
            table.addCell(cell);
        }

        return table;
    }

    private void fillCell(Context context, long receiptId, CellViewModel cell){

        List<Field> fields = new FieldDAO(context).findLogicsForRecord(String.valueOf(cell.getId()));

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        HistoryDAO historyDAO = new HistoryDAO(context);

        for (Field field : fields){
            History history = historyDAO.findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));

            if(history != null && history.getReceipt().getId() == receiptId){
                field.setValue(history.getValue());
            }

            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());
            fieldViewModel.setLabel(field.getColumn().getName());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }
}
