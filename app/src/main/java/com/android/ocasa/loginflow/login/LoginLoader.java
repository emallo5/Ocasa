package com.android.ocasa.loginflow.login;

import android.content.Context;

import com.codika.androidmvp.loader.PresenterLoader;

/**
 * Created by ignacio on 27/07/16.
 */
public class LoginLoader extends PresenterLoader<LoginPresenter> {

    public LoginLoader(Context context) {
        super(context);
    }

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter();
    }
}
