package com.android.ocasa.api;

import android.util.Log;

import rx.Subscriber;

/**
 * Created by ignacio on 22/08/16.
 */
public abstract class ApiSubscriber<T, E> extends Subscriber<T> {

    static final String TAG = "ApiSubscriber";

    private Class<E> error;

    public ApiSubscriber(Class<E> errorClass){
        this.error = errorClass;
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException = (ApiException) e;

        Log.v(TAG, apiException.getKind().name());

        switch (apiException.getKind()){
            case HTTP:
                onHttpError(apiException.getErrorBodyAs(error));
                break;
            case NETWORK:
                onNetworkError();
                break;
            case UNAUTHORIZED:
                onUnauthorizedError();
                break;
        }
    }

    public void onHttpError(E error){}

    public void onNotFoundError(){}

    public void onNetworkError(){}

    public void onUnauthorizedError(){}
}
