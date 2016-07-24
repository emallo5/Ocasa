package com.android.ocasa.receipt.item.detail;

import android.content.Context;

import com.android.ocasa.core.FormLoader;
import com.android.ocasa.core.FormPresenter;
import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 18/07/16.
 */
public class DetailReceiptItemLoader extends FormLoader{

    public DetailReceiptItemLoader(Context context) {
        super(context);
    }

    @Override
    public FormPresenter getPresenter() {
        return new DetailReceiptItemPresenter();
    }
}
