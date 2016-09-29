package com.android.ocasa.event;

import com.android.ocasa.viewmodel.CellViewModel;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptItemAddEvent {

    private int position;

    public ReceiptItemAddEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
