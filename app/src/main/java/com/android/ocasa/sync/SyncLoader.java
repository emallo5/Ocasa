package com.android.ocasa.sync;

import android.content.Context;

import com.android.ocasa.OcasaApplication;
import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 21/06/16.
 */
public class SyncLoader extends PresenterLoader<SyncPresenter> {

    public SyncLoader(Context context) {
        super(context);
    }

    @Override
    public SyncPresenter getPresenter() {
        return new SyncPresenter();
    }
}
