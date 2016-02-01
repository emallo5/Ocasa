package com.android.ocasa.service;

import android.content.Context;
import android.util.Log;

import com.android.ocasa.http.listener.RequestCallback;
import com.android.volley.VolleyError;

/**
 * Created by ignacio on 21/01/16.
 */
public abstract class GenericRequestCallback<T> implements RequestCallback<T> {

    static final String TAG = "RequestCallback";

    private Context context;

    public GenericRequestCallback(Context context){
        this.context = context;
    }

    @Override
    public void onSuccess(T response) {
        Log.v(TAG, "onSuccess");
    }

    @Override
    public void onError(VolleyError error) {
        Log.v(TAG, "Error: " + error.getMessage());
    }

    public Context getContext() {
        return context;
    }
}
