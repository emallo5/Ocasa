package com.android.ocasa.pickup.event;

/**
 * Created by Emiliano Mallo on 25/04/16.
 */
public class RemoveNotFoundItemEvent {

    private int position;

    public RemoveNotFoundItemEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
