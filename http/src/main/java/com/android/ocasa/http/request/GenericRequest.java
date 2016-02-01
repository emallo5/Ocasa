package com.android.ocasa.http.request;

import com.android.ocasa.http.listener.RequestListener;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Created by ignacio on 18/01/16.
 */
public class GenericRequest<T> extends Request<T>{

    private Class<T> clazz;
    private Gson gson;
    private RequestListener<T> listener;

    public GenericRequest(int method, String url, Class<T> clazz, Gson gson, RequestListener<T> listener) {
        super(method, url, listener);
        this.listener = listener;
        this.clazz = clazz;
        this.gson = gson;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
