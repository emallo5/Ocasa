package com.android.ocasa.home.menu;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 18/07/16.
 */
public class MenuLoader extends PresenterLoader<MenuPresenter> {

    public MenuLoader(Context context) {
        super(context);
    }

    @Override
    public MenuPresenter getPresenter() {
        return new MenuPresenter();
    }
}
