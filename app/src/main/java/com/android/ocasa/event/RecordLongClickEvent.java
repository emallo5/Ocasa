package com.android.ocasa.event;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class RecordLongClickEvent {

    private int position;

    public RecordLongClickEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
