package com.android.ocasa.receipt.list;

import com.android.ocasa.httpmodel.ControlResponse;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 11/07/16.
 */
public interface ReceiptListView extends BaseView {

    void onReceiptsLoadSuccess(ReceiptTableViewModel table);
    void onCloseReceiptSuccess();
    void onControlSynResponse(ControlResponse response);
    void onSyncSuccess();
    void onSyncError();
}
