package com.android.ocasa.receipt.list;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptListLoader extends PresenterLoader<ReceiptListPresenter> {

    public ReceiptListLoader(Context context) {
        super(context);
    }

    @Override
    public ReceiptListPresenter getPresenter() {
        return new ReceiptListPresenter();
    }
}
