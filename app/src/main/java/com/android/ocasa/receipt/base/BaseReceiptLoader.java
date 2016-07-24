package com.android.ocasa.receipt.base;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 07/07/16.
 */
public class BaseReceiptLoader extends PresenterLoader<BaseReceiptPresenter> {

    public BaseReceiptLoader(Context context) {
        super(context);
    }

    @Override
    public BaseReceiptPresenter getPresenter() {
        return new BaseReceiptPresenter();
    }
}
