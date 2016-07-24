package com.android.ocasa.receipt.base;

import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 07/07/16.
 */
public interface BaseReceiptView extends BaseView {

    void onHeaderSuccess(ReceiptFormViewModel header);
    void onItemsSuccess(TableViewModel table);
}
