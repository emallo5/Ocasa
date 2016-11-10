package com.android.ocasa.event;

/**
 * Created by ignacio on 10/11/16.
 */

public class CloseReceiptEvent {

    private long receiptId;

    public CloseReceiptEvent(long receiptId) {
        this.receiptId = receiptId;
    }

    public long getReceiptId() {
        return receiptId;
    }
}
