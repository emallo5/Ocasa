package com.android.ocasa.http.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by ignacio on 18/01/16.
 */
public class RequestListener<T> implements Response.ErrorListener, Response.Listener<T>{

    private RequestCallback<T> callback;

    public RequestListener(RequestCallback<T> callback){
        this.callback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        callback.onError(error);
    }

    @Override
    public void onResponse(T response) {
        callback.onSuccess(response);
    }
}
