package com.android.ocasa.model;

import com.android.ocasa.util.ConfigHelper;

import java.util.ArrayList;

/**
 * Created by ignacio on 07/02/17.
 */

public class UploadLog {

    public static final String TAG = "logs";

    private ArrayList<ReceiptLog> logs = new ArrayList<>();



    public ArrayList<ReceiptLog> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<ReceiptLog> logs) {
        this.logs = logs;
    }
}
