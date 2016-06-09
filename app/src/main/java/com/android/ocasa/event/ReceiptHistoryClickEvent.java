package com.android.ocasa.event;

/**
 * Created by Emiliano Mallo on 05/05/16.
 */
public class ReceiptHistoryClickEvent {

    private int historyId;

    public ReceiptHistoryClickEvent(int historyId) {
        this.historyId = historyId;
    }

    public int getHistoryId() {
        return historyId;
    }
}
