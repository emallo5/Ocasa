package com.android.ocasa.viewmodel;

/**
 * Created by ignacio on 21/06/16.
 */
public class ReceiptCellViewModel extends CellViewModel{

    private boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
