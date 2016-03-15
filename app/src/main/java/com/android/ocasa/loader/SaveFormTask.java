package com.android.ocasa.loader;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.ocasa.sync.SendService;
import com.android.ocasa.util.DateTimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ignacio on 11/02/16.
 */
public class SaveFormTask extends AsyncTask<SaveFormTask.FormData, Void, Void> {

    private Context context;

    public SaveFormTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(FormData... data) {

        FormData formdata = data[0];

        long[] recordIds = formdata.getRecordIds();

        List<Record> records = new ArrayList<>();

        for (long recordId : recordIds) {
            Record record = updateRecord(formdata.getValues(), recordId, formdata.getLastLocation(), formdata.getValidityDate());
            records.add(record);
        }

        if(records.size() > 1){
            generateReceipt(records);
        }

        return null;
    }

    private Record updateRecord(Map<String, String> formValues, long recordId, Location lastLocation, Calendar validityDate){

        Record record = new RecordDAO(context).finById(recordId);

        for (Field field : record.getFields()){

            String value = formValues.get(field.getColumn().getId());

            if(!value.isEmpty() && !value.equalsIgnoreCase(field.getValue())) {

                History history = new History();
                history.setValue(value);
                history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
                history.setValidityDate(DateTimeHelper.formatDateTime(validityDate.getTime()));
                history.setField(field);

                if(lastLocation != null) {
                    history.setLongitude(lastLocation.getLongitude());
                    history.setLatitude(lastLocation.getLatitude());
                }

                history.setTimeZone(DateTimeHelper.getDeviceTimezone());

                field.addHistory(history);

                field.setValue(value);
            }
        }

        new RecordService(context).saveRecord(record);

        return record;
    }

    private void generateReceipt(List<Record> records){

        Receipt receipt = new Receipt();
        receipt.setRecords(records);

        for (Record record : records){
            record.setReceipt(receipt);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        context.startService(new Intent(context, SendService.class));

        NotificationManager.sendBroadcast(context, RecordService.RECORD_SYNC_FINISHED_ACTION);
    }

    public static class FormData{

        private Map<String, String> values;
        private long[] recordIds;
        private Location lastLocation;
        private Calendar validityDate;

        public FormData(Map<String, String> values, long recordId, Location lastLocation){
            this.values = values;
            this.recordIds = new long[1];
            this.recordIds[0] = recordId;
            this.lastLocation = lastLocation;
            this.validityDate = Calendar.getInstance();
        }

        public FormData(Map<String, String> values, long[] recordIds, Location lastLocation, Calendar validityDate){
            this.values = values;
            this.recordIds = recordIds;
            this.lastLocation = lastLocation;
            this.validityDate = validityDate;
        }

        public Map<String, String> getValues() {
            return values;
        }

        public long[] getRecordIds() {
            return recordIds;
        }

        public Location getLastLocation() {
            return lastLocation;
        }

        public Calendar getValidityDate() {
            return validityDate;
        }
    }

}
