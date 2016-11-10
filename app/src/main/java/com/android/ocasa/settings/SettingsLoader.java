package com.android.ocasa.settings;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 10/11/16.
 */

public class SettingsLoader extends PresenterLoader<SettingsPresenter> {

    public SettingsLoader(Context context) {
        super(context);
    }

    @Override
    public SettingsPresenter getPresenter() {
        return new SettingsPresenter();
    }
}
