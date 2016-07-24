package com.android.ocasa.core;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 18/07/16.
 */
public class TableLoader extends PresenterLoader<TablePresenter> {

    public TableLoader(Context context) {
        super(context);
    }

    @Override
    public TablePresenter getPresenter() {
        return new TablePresenter();
    }
}
