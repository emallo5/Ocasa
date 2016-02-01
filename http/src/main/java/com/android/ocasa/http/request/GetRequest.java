package com.android.ocasa.http.request;

import com.android.ocasa.http.listener.RequestListener;
import com.google.gson.Gson;

/**
 * Created by ignacio on 18/01/16.
 */
public class GetRequest<T> extends GenericRequest<T> {

    public GetRequest(String url, Class<T> clazz, Gson gson, RequestListener<T> listener) {
        super(Method.GET, url, clazz, gson, listener);
    }
}
