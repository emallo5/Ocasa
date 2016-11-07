package com.android.ocasa.receipt.edit;

import com.android.ocasa.viewmodel.CellViewModel;

/**
 * Created by ignacio on 03/08/16.
 */
public interface OnItemChangeListener {
    void onShowSearchResults();
    void onItemAdded(CellViewModel item);
    void onItemRemoved(CellViewModel item);
    void onItemNotFound(String code);
    void onPreviousItemsLoaded(long[] recordIds);
}
