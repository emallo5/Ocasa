package com.android.ocasa.receipt.item.detailaction;

import android.content.Context;

import com.android.ocasa.core.FormPresenter;
import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 24/10/16.
 */

public class DetailActionLoader extends PresenterLoader<FormPresenter> {

    public DetailActionLoader(Context context) {
        super(context);
    }

    @Override
    public DetailActionPresenter getPresenter() {
        return new DetailActionPresenter();
    }
}
