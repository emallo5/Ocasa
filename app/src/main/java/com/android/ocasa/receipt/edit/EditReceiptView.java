package com.android.ocasa.receipt.edit;

import com.android.ocasa.receipt.base.BaseReceiptView;
import com.android.ocasa.viewmodel.CellViewModel;

import java.util.List;

/**
 * Created by ignacio on 07/07/16.
 */
public interface EditReceiptView extends BaseReceiptView {
    void onItemsFoundSuccess(List<CellViewModel> items);
    void onReceiptItemsEmpty();
    void onReceiptSaveSuccess();
    void onTakeSignature(String fieldName);
    void onTakeText(String fieldName);
    void showProgress();
    void hideProgress();
}
