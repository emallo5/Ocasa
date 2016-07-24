package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptTableViewModel extends TableViewModel {

    private List<ReceiptCellViewModel> receipts;

    public ReceiptTableViewModel(){
        receipts = new ArrayList<>();
    }


    public List<ReceiptCellViewModel> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<ReceiptCellViewModel> receipts) {
        this.receipts = receipts;
    }
}
