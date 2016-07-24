package com.android.ocasa.core;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 11/07/16.
 */
public class FormLoader extends PresenterLoader<FormPresenter> {

    public FormLoader(Context context) {
        super(context);
    }

    @Override
    public FormPresenter getPresenter() {
        return new FormPresenter();
    }
}
