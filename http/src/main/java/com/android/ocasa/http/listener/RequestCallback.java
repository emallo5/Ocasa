package com.android.ocasa.http.listener;

import com.android.volley.VolleyError;

/**
 * Created by ignacio on 18/01/16.
 */
public interface RequestCallback<T> {

    public void onSuccess(T response);
    public void onError(VolleyError error);
}
