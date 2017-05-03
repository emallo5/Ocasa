package com.android.ocasa.viewmodel;

/**
 * Created by ignacio on 21/06/16.
 */
public class ReceiptCellViewModel extends CellViewModel{

    private boolean isOpen;

    private boolean isCreated;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
