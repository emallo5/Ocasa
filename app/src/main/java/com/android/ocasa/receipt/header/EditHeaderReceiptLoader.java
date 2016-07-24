package com.android.ocasa.receipt.header;

import android.content.Context;

import com.android.ocasa.core.FormLoader;
import com.android.ocasa.core.FormPresenter;

/**
 * Created by ignacio on 11/07/16.
 */
public class EditHeaderReceiptLoader extends FormLoader {

    public EditHeaderReceiptLoader(Context context) {
        super(context);
    }

    @Override
    public FormPresenter getPresenter() {
        return new EditHeaderReceiptPresenter();
    }
}
