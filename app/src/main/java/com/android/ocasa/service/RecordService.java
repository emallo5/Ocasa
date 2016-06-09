package com.android.ocasa.service;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.http.service.HttpService;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordService {

    public static final String RECORD_SYNC_FINISHED_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_FINISHED_ACTION";
    public static final String RECORD_SYNC_ERROR_ACTION = "com.android.ocasa.service.RecordService.RECORD_SYNC_ERROR_ACTION";

    static final String TABLE_PATH = "table";
    static final String RECORD_PATH = "record";

    private Context context;

    public RecordService(Context context){
        this.context = context;
    }

    public void syncRecord(String tableId, GenericRequestCallback<TableRecord> callback){

        HttpService service = HttpService.getInstance(context);

        Gson gson = new GsonBuilder().registerTypeAdapter(Record.class, new TableRecord.RecordDeserializer()).create();

        service.newGetRequest(buildRecordUrl(tableId), TableRecord.class, gson, callback);
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

    public FormViewModel getFormFromRecord(long recordId){
          return getFormFromRecordAndReceipt(recordId, -1);
    }

    public FormViewModel getFormFromRecordAndReceipt(long recordId, long receiptId){

        RecordDAO recordDAO = RecordDAO.getInstance(context);

        Record record;

        if(receiptId == -1)
            record = recordDAO.findById(recordId);
        else
            record = getRecordFromReceipt(recordId, receiptId);

        FormViewModel form = convertRecord(record);

        List<Field> fields = new ArrayList<>(record.getFields());

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            History history = new HistoryDAO(context).findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));

            FieldViewModel fieldViewModel = convertField(field);

            if(history != null && history.getReceipt().getId() == receiptId){
                fieldViewModel.setValue(history.getValue());
            }

            if(field.getColumn().getFieldType() == FieldType.COMBO){

                fieldViewModel.setRelationshipTable(field.getColumn().getRelationship().getId());
                fieldViewModel.setRelationshipFields(getComboFields(field));
            }

            form.addField(fieldViewModel);
        }

        return form;
    }

    public FormViewModel getFormFromRecordAndAction(long recordId, String action){

        Record record = getRecordFromAction(recordId, action);

        FormViewModel form = convertRecord(record);

        List<Field> fields = new ArrayList<>(record.getFields());

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            FieldViewModel fieldViewModel = convertField(field);

            if(field.getColumn().getFieldType() == FieldType.COMBO){

                fieldViewModel.setRelationshipTable(field.getColumn().getRelationship().getId());
                fieldViewModel.setRelationshipFields(getComboFields(field));
            }

            form.addField(fieldViewModel);
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

        Column primaryColumn = new ColumnDAO(context).findPrimaryKeyColumnForTable(field.getColumn().getRelationship().getId());

        Record record = RecordDAO.getInstance(context).findForColumnAndValue(primaryColumn.getId(), field.getValue());

        List<Field> fields = record.getLogicFields();

        for (int index = 0; index < fields.size(); index++){

            Field comboField = fields.get(index);

            relationship.add(convertField(comboField));
        }

        return relationship;
    }

    private String buildRecordUrl(String tableId){

        Uri builtUri = Uri.parse(BuildConfig.BASE_URL)
                .buildUpon()
                .appendPath("android")
                .appendPath(TABLE_PATH)
                .appendPath(tableId)
                .appendPath(RECORD_PATH)
                .appendPath(RECORD_PATH + tableId + ".json").build();

        return builtUri.toString();
    }

    public static class SaveRecordResponseCallback extends GenericRequestCallback<TableRecord> {

        private Table table;

        public SaveRecordResponseCallback(Context context, String tableId) {
            super(context);
            this.table = new Table();
            this.table.setId(tableId);
        }

        @Override
        public void onSuccess(TableRecord response) {
            super.onSuccess(response);

            saveTableRecord(response);
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);

            NotificationManager.sendBroadcast(getContext(), RECORD_SYNC_ERROR_ACTION);
        }

        private void saveTableRecord(TableRecord tableRecord){

            new AsyncTask<TableRecord, Void, Void>(){

                @Override
                protected Void doInBackground(TableRecord... tableRecords) {

                    RecordDAO recordDAO = RecordDAO.getInstance(getContext());
                    recordDAO.deleteForTable(table.getId());
                    HistoryDAO historyDAO = new HistoryDAO(getContext());

                    FieldDAO dao = new FieldDAO(getContext());

                    for (Record record : tableRecords[0].getRecords()){
                        record.setTable(table);

                        if(record.isUpdated()){
                            Record updated = recordDAO.findByExternalId(record.getExternalId());
                            if(updated != null) {
                                record.setId(updated.getId());
                                recordDAO.update(record);
                            }else{
                                recordDAO.save(record);
                            }
                        }else{
                            recordDAO.save(record);
                        }

                        for (Field field : record.getFields()){
                            field.setRecord(record);

                            History history = new History();
                            history.setValue(field.getValue());
                            history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
                            history.setField(field);
                            history.setTimeZone(DateTimeHelper.getDeviceTimezone());

                            field.addHistory(history);

                            dao.save(field);
                            historyDAO.save(history);
                        }
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    NotificationManager.sendBroadcast(getContext(), RECORD_SYNC_FINISHED_ACTION);
                }
            }.execute(tableRecord);


        }
    }

}
