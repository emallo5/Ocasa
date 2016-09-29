package com.android.ocasa.receipt.item.list;

import android.content.Context;

import com.android.ocasa.core.TableLoader;
import com.android.ocasa.core.TablePresenter;

/**
 * Created by ignacio on 03/08/16.
 */
public class ReceiptItemsLoader extends TableLoader {

    public ReceiptItemsLoader(Context context) {
        super(context);
    }

    @Override
    public TablePresenter getPresenter() {
        return new ReceiptItemsPresenter();
    }
}
