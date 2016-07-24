package com.android.ocasa.receipt.edit;

import android.content.Context;

import com.android.ocasa.receipt.base.BaseReceiptLoader;
import com.android.ocasa.receipt.base.BaseReceiptPresenter;

/**
 * Created by ignacio on 07/07/16.
 */
public class EditReceiptLoader extends BaseReceiptLoader {

    public EditReceiptLoader(Context context) {
        super(context);
    }

    @Override
    public BaseReceiptPresenter getPresenter() {
        return new EditReceiptPresenter();
    }
}
