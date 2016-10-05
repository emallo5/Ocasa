package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.LayoutDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordService {

    public static final String RECORD_SYNC_FINISHED_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_FINISHED_ACTION";
    public static final String RECORD_SYNC_ERROR_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_ERROR_ACTION";

    private Context context;

    public RecordService(Context context){
        this.context = context;
    }

    public void saveRecord(Record record){

        RecordDAO.getInstance(context).update(record);

        HistoryDAO historyDAO = new HistoryDAO(context);

        for (Field field : record.getFields()){
            historyDAO.save(field.getHistorical());
        }

        FieldDAO dao = new FieldDAO(context);
        dao.update(record.getFields());
    }

    public void updateRecord(Record record){
        RecordDAO.getInstance(context).update(record);

        FieldDAO dao = new FieldDAO(context);
        dao.update(record.getFields());
    }

    public FormViewModel getFormFromRecord(long recordId){
          return getFormFromRecordAndReceipt(recordId, -1);
    }

    public Record findById(long recordId){
        return RecordDAO.getInstance(context).findById(recordId);
    }

    public FormViewModel getFormFromRecordAndReceipt(long recordId, long receiptId){

        RecordDAO recordDAO = RecordDAO.getInstance(context);

        Record record;

        if(receiptId == -1)
            record = recordDAO.findById(recordId);
        else
            record = getRecordFromReceipt(recordId, receiptId);

        Table table = new TableDAO(context).findById(record.getTable().getId());

//        Category category = table.getCategory();

//        if(category == null){
//            Receipt receipt = new ReceiptDAO(context).findById(receiptId);
//            category = new CategoryDAO(context).findById(receipt.getAction().getCategory().getId());
//        }else{
//            category = new CategoryDAO(context).findById(category.getId());
//        }

//        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        FormViewModel form = convertRecord(record);
        form.setTitle(table.getName());
        form.setColor("#33BDC2");

        List<Field> fields = new ArrayList<>(record.getFields());

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            History history = new HistoryDAO(context).findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));

            FieldViewModel fieldViewModel = convertField(field);

            if(history != null && history.getReceipt().getId() == receiptId){
                fieldViewModel.setValue(history.getValue());
                field.setValue(history.getValue());
            }

            if(field.getColumn().getFieldType() == FieldType.COMBO){

                fieldViewModel.setRelationshipTable(field.getColumn().getRelationship().getExternalID());
                fieldViewModel.setRelationshipFields(getComboFields(field));
            }

            form.addField(fieldViewModel);
        }

        return form;
    }

    public FormViewModel getFromFromRecordAndReceipt(long recordId, long receiptId){
        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        return getFormFromRecordAndAction(recordId, receipt.getAction().getId());
    }

    public FormViewModel getFormFromRecordAndAction(long recordId, String action){

        Record record = getRecordFromAction(recordId, action);

        Table table = new TableDAO(context).findById(record.getTable().getId());

        Category category = new CategoryDAO(context).findById(table.getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        FormViewModel form = convertRecord(record);
        form.setTitle(table.getName());
        form.setColor(application.getRecordColor());

        List<Field> fields = new ArrayList<>(record.getFields());

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            FieldViewModel fieldViewModel = convertField(field);

            if(field.getColumn().getFieldType() == FieldType.COMBO){

                fieldViewModel.setRelationshipTable(field.getColumn().getRelationship().getExternalID());
                fieldViewModel.setRelationshipFields(getComboFields(field));
            }

            form.addField(fieldViewModel);
        }

        return form;
    }

    public FormViewModel getFormFromTable(String tableId){

        FormViewModel form = new FormViewModel();

        Layout layout = new LayoutDAO(context).findByExternalId(tableId);

        Table table = layout.getTable();
        form.setTitle(table.getName());

//        Category category = new CategoryDAO(context).findById(table.getCategory().getId());
//
//        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());
//
        form.setColor("#33BDC2");

        List<Column> columns = new ColumnDAO(context).findColumnsForLayout(layout.getExternalID(), table.getId());

        for (int index = 0; index < columns.size(); index++){

            Column column = columns.get(index);

            FieldViewModel field = convertColumn(column);

            if(column.getFieldType() == FieldType.COMBO){
                field.setRelationshipTable(column.getRelationship().getExternalID());
                field.setRelationshipFields(getComboColumns(column));
            }

            form.addField(field);
        }

        return form;
    }

    private Record getRecordFromReceipt(long recordId, long receiptId){

        Record record = RecordDAO.getInstance(context).findById(recordId);

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        record.setFields(new FieldDAO(context)
                .findForAvailableColumns(recordId,
                        receipt.getAction().getDetailsComlumIds()));

        return record;
    }

    private Record getRecordFromAction(long recordId, String actionId){

        Record record = RecordDAO.getInstance(context).findById(recordId);

        Action action = new ActionDAO(context).findById(actionId);

        action.setColumnsDetail(new ColumnActionDAO(context).findColumnsForActionAndType(actionId, ColumnAction.ColumnActionType.DETAIL));

        record.setFields(new FieldDAO(context)
                .findForAvailableColumns(recordId,
                        action.getDetailsComlumIds()));

        return record;
    }

    private FormViewModel convertRecord(Record record){
        FormViewModel form = new FormViewModel();

        form.setTitle(record.getTable().getName());
        form.setId(record.getId());

        return form;
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

    private List<FieldViewModel> getComboFields(Field field){
        List<FieldViewModel> relationship = new ArrayList<>();

        Column primaryColumn = new ColumnDAO(context).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getExternalID(),
                field.getColumn().getRelationship().getTable().getId());

        Record record = RecordDAO.getInstance(context).findForColumnAndValue(primaryColumn.getId(), field.getValue());

        if(record == null)
            return relationship;

        List<Field> fields = record.getLogicFields();

        for (int index = 0; index < fields.size(); index++){

            Field comboField = fields.get(index);

            relationship.add(convertField(comboField));
        }

        return relationship;
    }

    private List<FieldViewModel> getComboColumns(Column column){
        List<FieldViewModel> relationship = new ArrayList<>();

        List<Column> columns = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getExternalID());

        for (int index = 0; index < columns.size(); index++){

            Column comboColumn= columns.get(index);

            relationship.add(convertColumn(comboColumn));
        }

        return relationship;
    }
}
