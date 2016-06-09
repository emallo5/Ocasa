package com.android.ocasa.event;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ReceiptItemDeleteEvent {

    private long id;
    private int position;

    public ReceiptItemDeleteEvent(long id, int position) {
        this.id = id;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

}
