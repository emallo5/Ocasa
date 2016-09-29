package com.android.ocasa.session;

import com.android.ocasa.api.OcasaApiSubscriber;

/**
 * Created by ignacio on 08/09/16.
 */
public abstract class SessionSubscriber<T> extends OcasaApiSubscriber<T> {

    private SessionPresenter<? extends SessionView> presenter;

    protected SessionSubscriber(SessionPresenter<? extends SessionView> presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onUnauthorizedError() {
        super.onUnauthorizedError();
        presenter.getView().onUnauthorizedError();
    }
}
