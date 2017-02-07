package com.android.ocasa.util;

import com.android.ocasa.model.ReceiptLog;
import com.android.ocasa.model.UploadLog;

import java.util.ArrayList;

import static com.android.ocasa.model.UploadLog.TAG;

/**
 * Created by ignacio on 07/02/17.
 */

public class LogHelper {

    public static void addLog(String id) {
        UploadLog loger = (UploadLog) ConfigHelper.getInstance().ReadObjectConfig(TAG, UploadLog.class);
        if (loger == null) loger = new UploadLog();

        loger.getLogs().add(new ReceiptLog(id));

        ConfigHelper.getInstance().WriteObjectConfig(TAG, loger);
    }

    public static void addResponseImage(String id, String response) {
        UploadLog loger = (UploadLog) ConfigHelper.getInstance().ReadObjectConfig(TAG, UploadLog.class);
        for (ReceiptLog log : loger.getLogs()) {
            if (log.getId().equals(id)) {
                log.setUploadImage(response);
                break;
            }
        }
        ConfigHelper.getInstance().WriteObjectConfig(TAG, loger);
    }

    public static void addResponseData(String id, String response) {
        UploadLog loger = (UploadLog) ConfigHelper.getInstance().ReadObjectConfig(TAG, UploadLog.class);
        for (ReceiptLog log : loger.getLogs()) {
            if (log.getId().equals(id)) {
                log.setUploadData(response);
                break;
            }
        }
        ConfigHelper.getInstance().WriteObjectConfig(TAG, loger);
    }

    public static void resetLog() {
        ConfigHelper.getInstance().WriteObjectConfig(TAG, "");
    }
}
