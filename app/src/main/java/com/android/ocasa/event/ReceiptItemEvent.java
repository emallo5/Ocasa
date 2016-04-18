package com.android.ocasa.event;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ReceiptItemEvent {

    private long recordId;
    private int position;

    public ReceiptItemEvent(long recordId, int position) {
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
