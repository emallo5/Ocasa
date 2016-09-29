package com.android.ocasa.session;

import com.codika.androidmvprx.presenter.BaseRxPresenter;

/**
 * Created by ignacio on 08/09/16.
 */
public abstract class SessionPresenter<V extends SessionView> extends BaseRxPresenter<V> {

    public void cleanSession(){
        SessionManager.getInstance().cleanSession();
    }
}
