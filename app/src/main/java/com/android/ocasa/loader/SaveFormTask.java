package com.android.ocasa.loader;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.ocasa.sync.SendService;
import com.android.ocasa.util.DateTimeHelper;
import com.android.volley.Response;

import java.util.ArrayList;
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

        long recordId = formdata.getRecordId();

        updateRecord(formdata.getValues(), recordId, formdata.getLastLocation());

        return null;
    }

    private Record updateRecord(Map<String, String> formValues, long recordId, Location lastLocation){

        Record record = RecordDAO.getInstance(context).findById(recordId);

        List<Field> fields = new ArrayList<>(record.getFields());

        for (int index = 0; index < fields.size(); index++){

            Field field = fields.get(index);

            String value = formValues.get(field.getColumn().getId());

            if(!value.isEmpty() && !value.equalsIgnoreCase(field.getValue())) {
                record.updateStatus();

                History history = new History();
                history.setValue(value);
                history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        context.startService(new Intent(context, SendService.class));

        NotificationManager.sendBroadcast(context, RecordService.RECORD_SYNC_FINISHED_ACTION);
    }

    public static class FormData{

        private Map<String, String> values;
        private long recordId;
        private Location lastLocation;

        public FormData(Map<String, String> values, long recordId, Location lastLocation){
            this.values = values;
            this.recordId = recordId;
            this.lastLocation = lastLocation;
        }

        public Map<String, String> getValues() {
            return values;
        }

        public long getRecordId() {
            return recordId;
        }

        public Location getLastLocation() {
            return lastLocation;
        }
    }

}
