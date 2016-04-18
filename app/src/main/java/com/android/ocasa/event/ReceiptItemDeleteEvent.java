package com.android.ocasa.event;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ReceiptItemDeleteEvent {

    private int position;

    public ReceiptItemDeleteEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
