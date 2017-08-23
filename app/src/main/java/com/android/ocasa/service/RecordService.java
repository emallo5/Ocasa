package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.cache.dao.ActionDAO;
import com.android.ocasa.cache.dao.ColumnActionDAO;
import com.android.ocasa.cache.dao.ColumnDAO;
import com.android.ocasa.cache.dao.FieldDAO;
import com.android.ocasa.cache.dao.HistoryDAO;
import com.android.ocasa.cache.dao.LayoutDAO;
import com.android.ocasa.cache.dao.ReceiptDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordService {

    public static final String RECORD_SYNC_FINISHED_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_FINISHED_ACTION";

    public void saveRecord(Context context, Record record){

        new RecordDAO(context).update(record);

        HistoryDAO historyDAO = new HistoryDAO(context);

        for (Field field : record.getFields()){
            historyDAO.save(field.getHistorical());
        }

        FieldDAO dao = new FieldDAO(context);
        dao.update(record.getFields());
    }

    public void saveRecordsFromTable(Context context, Table table){

        if(table.getRecords() == null)
            return;

        RecordDAO recordDAO = new RecordDAO(context);

        HistoryDAO historyDAO = new HistoryDAO(context);

        FieldDAO fieldDAO = new FieldDAO(context);

        List<Record> records = new ArrayList<>(table.getRecords());

        List<Field> fields = new ArrayList<>();
        List<History> histories = new ArrayList<>();

        for (int index = 0; index <  records.size(); index++){

            Record record = records.get(index);

//            if(table.getId().equalsIgnoreCase("OM_MovilNovedad")){
//
//                Column column = new Column();
//                column.setId("OM_MOVILNOVEDAD_CF_0400");
//                column.setVisible(false);
//
//                Field signature = new Field();
//                signature.setColumn(column);
//                signature.setValue("");
//
//                record.getFields().add(signature);
//
//                Column photoColumn = new Column();
//                photoColumn.setId("OM_MOVILNOVEDAD_CF_0500");
//                photoColumn.setVisible(false);
//
//                Field photo = new Field();
//                photo.setColumn(photoColumn);
//                photo.setValue("");
//
//                record.getFields().add(photo);
//
//                Column photoColumn1 = new Column();
//                photoColumn1.setId("OM_MOVILNOVEDAD_CF_0600");
//                photoColumn1.setVisible(false);
//
//                Field photo1 = new Field();
//                photo1.setColumn(photoColumn1);
//                photo1.setValue("");
//
//                record.getFields().add(photo1);
//
//                Column photoColumn2 = new Column();
//                photoColumn2.setId("OM_MOVILNOVEDAD_CF_0700");
//                photoColumn2.setVisible(false);
//
//                Field photo2 = new Field();
//                photo2.setColumn(photoColumn2);
//                photo2.setValue("");
//
//                record.getFields().add(photo2);
//
//                Column photoColumn3 = new Column();
//                photoColumn3.setId("OM_MOVILNOVEDAD_CF_0800");
//                photoColumn3.setVisible(false);
//
//                Field photo3 = new Field();
//                photo3.setColumn(photoColumn3);
//                photo3.setValue("");
//
//                record.getFields().add(photo3);
//            }

            record.setExternalId(record.getExternalId());
            record.setTable(table);
            record.fillConcatValues();

            Record updated = recordDAO.findByExternalId(record.getExternalId());

            if(updated != null){
                record.setId(updated.getId());
            }

            for (Field field : record.getFields()){
                Field newField = newField(record, field, updated != null);

                if(newField != null) {
                    fields.add(newField);

                    if (newField.getHistorical() != null)
                        histories.addAll(newField.getHistorical());
                }
            }
        }

        recordDAO.save(records);
        fieldDAO.save(fields);
        historyDAO.save(histories);
    }

    private History newHistory(Field field){
        History history = new History();
        history.setValue(field.getValue());
        history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
        history.setField(field);
        history.setTimeZone(DateTimeHelper.getDeviceTimezone());

        return history;
    }

    private Field newField(Record record, Field field, boolean isUpdated){

        field.setRecord(record);

        if(isUpdated){
            Field updateField = record.getFieldForColumn(field.getColumn().getId());
            if(updateField != null && !field.getValue().equalsIgnoreCase(updateField.getValue())) {
                field.addHistory(newHistory(field));
                updateField.setValue(field.getValue());

                return updateField;
            }

            return null;
        }else{
            field.addHistory(newHistory(field));
        }

        return field;
    }

    public FormViewModel getFormFromRecord(Context context, long recordId){
          return getFormFromRecordAndReceipt(context, recordId, -1);
    }

    public Record findRecord(Context context, long recordId){
        return new RecordDAO(context).findById(recordId);
    }

    public FormViewModel getFormFromRecordAndReceipt(Context context, long recordId, long receiptId){

        Record record;

        if(receiptId == -1)
            record = findRecord(context, recordId);
        else
            record = getRecordFromReceipt(context, recordId, receiptId);

        FormViewModel form = new TableService().getFormForTable(context, record.getTable().getId());
        form.setId(recordId);

        List<Field> fields = new ArrayList<>(record.getFields());

        FieldService service = new FieldService();

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            History history = new HistoryDAO(context).findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));

            if(history != null && history.getReceipt().getId() == receiptId){
                field.setValue(history.getValue());
            }

            FieldViewModel fieldViewModel = service.getFieldFromField(context, field);

            form.addField(fieldViewModel);
        }

        return form;
    }

    public FormViewModel getFromFromRecordAndReceipt(Context context, long recordId, long receiptId){
        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        return getFormFromRecordAndAction(context, recordId, receipt.getAction().getId());
    }

    public FormViewModel getFormFromRecordAndAction(Context context, long recordId, String action){

        Record record = getRecordFromAction(context, recordId, action);

        FormViewModel form = new TableService().getFormForTable(context, record.getTable().getId());
        form.setId(recordId);

        List<Field> fields = new ArrayList<>(record.getFields());

        FieldService service = new FieldService();

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            form.addField(service.getFieldFromField(context, field));
        }

        return form;
    }

    public FormViewModel getFormFromTable(Context context, String tableId){

        Layout layout = new LayoutDAO(context).findByExternalId(tableId);

        FormViewModel form = new TableService().getFormForTable(context, layout.getTable().getId());

        List<Column> columns = new ColumnDAO(context)
                .findColumnsForLayout(layout.getExternalID(), layout.getTable().getId());

        FieldService service = new FieldService();

        for (int index = 0; index < columns.size(); index++){

            Column column = columns.get(index);

            FieldViewModel field = service.getFieldFromColumn(context, column);

            form.addField(field);
        }

        return form;
    }

    private Record getRecordFromReceipt(Context context, long recordId, long receiptId){

        Record record = findRecord(context, recordId);

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        record.setFields(new FieldDAO(context)
                .findForAvailableColumns(recordId,
                        receipt.getAction().getDetailsComlumIds()));

        return record;
    }

    private Record getRecordFromAction(Context context, long recordId, String actionId){

        Record record = findRecord(context, recordId);

        Action action = new ActionDAO(context).findById(actionId);

        action.setColumnsDetail(new ColumnActionDAO(context).findColumnsForActionAndType(actionId, ColumnAction.ColumnActionType.DETAIL));

        record.setFields(new FieldDAO(context)
                .findForAvailableColumns(recordId,
                        action.getDetailsComlumIds()));

        return record;
    }
}