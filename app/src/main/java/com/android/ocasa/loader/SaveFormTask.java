package com.android.ocasa.loader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.android.ocasa.model.Record;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.sync.SendService;

/**
 * Created by ignacio on 11/02/16.
 */
public class SaveFormTask extends AsyncTask<Record, Void, Void> {

    private Context context;

    public SaveFormTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Record... records) {
        new RecordService(context).saveRecord(records[0]);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        context.startService(new Intent(context, SendService.class));
    }
}
