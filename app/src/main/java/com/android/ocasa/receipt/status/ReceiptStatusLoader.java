package com.android.ocasa.receipt.status;

import android.content.Context;

import com.android.ocasa.core.FormLoader;
import com.android.ocasa.core.FormPresenter;

/**
 * Created by ignacio on 18/07/16.
 */
public class ReceiptStatusLoader extends FormLoader {

    public ReceiptStatusLoader(Context context) {
        super(context);
    }

    @Override
    public FormPresenter getPresenter() {
        return new ReceiptStatusPresenter();
    }
}
