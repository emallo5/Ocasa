package com.android.ocasa.event;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptItemAddEvent {

    private long recordId;
    private int position;

    public ReceiptItemAddEvent(long recordId, int position) {
        this.recordId = recordId;
        this.position = position;
    }

    public long getRecordId() {
        return recordId;
    }

    public int getPosition() {
        return position;
    }
}
