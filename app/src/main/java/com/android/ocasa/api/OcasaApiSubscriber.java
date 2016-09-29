package com.android.ocasa.api;

import com.android.ocasa.model.ApiError;

/**
 * Created by ignacio on 22/08/16.
 */
public abstract class OcasaApiSubscriber<T> extends ApiSubscriber<T, ApiError> {

    public OcasaApiSubscriber() {
        super(ApiError.class);
    }
}
