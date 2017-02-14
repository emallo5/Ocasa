package com.android.ocasa.util;

public class SyncUtil {

    private static final int SYNC_SUCCES = 100;
    private static final int SYNC_ERROR = 101;

    private static SyncUtil instance;
    private int state = SYNC_SUCCES;

    public static SyncUtil getInstance() {
        if (instance == null)
            instance = new SyncUtil();
        return instance;
    }

    public void setError(String message) {
        state = SYNC_ERROR;
        FileHelper.getInstance().writeToFile("sync: " + message);
    }

    public boolean wasError() {
        if (state == SYNC_SUCCES)
            return false;

        state = SYNC_SUCCES;
        return true;
    }
}
