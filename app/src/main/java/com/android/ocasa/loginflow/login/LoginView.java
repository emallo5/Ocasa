package com.android.ocasa.loginflow.login;

import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 27/07/16.
 */
public interface LoginView extends BaseView {
    void showEmptyFieldsAlert();
    void showInvalidEmail();
    void showProgress();
    void onLoginSuccess();
    void onLoginError(String error);
    void onNetworkError();
}
