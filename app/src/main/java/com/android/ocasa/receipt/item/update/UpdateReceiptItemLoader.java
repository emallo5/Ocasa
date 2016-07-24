package com.android.ocasa.receipt.item.update;

import android.content.Context;

import com.android.ocasa.core.FormLoader;
import com.android.ocasa.core.FormPresenter;

/**
 * Created by ignacio on 18/07/16.
 */
public class UpdateReceiptItemLoader extends FormLoader {

    public UpdateReceiptItemLoader(Context context) {
        super(context);
    }

    @Override
    public FormPresenter getPresenter() {
        return new UpdateReceiptItemPresenter();
    }
}
