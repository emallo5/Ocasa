package com.android.ocasa.http.service;

import android.content.Context;

import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.http.listener.RequestListener;
import com.android.ocasa.http.request.GetRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * Created by ignacio on 18/01/16.
 */
public class HttpService {

    private static HttpService instance;

    private Context context;

    public static HttpService getInstance(Context context){
        if(instance == null)
            instance = new HttpService(context);

        return instance;
    }

    private HttpService(Context context){
        this.context = context;
    }

    public <T> void newGetRequest(String url, Class<T> clazz, RequestCallback<T> callback){
        newGetRequest(url, clazz, new Gson(), callback);
    }

    public <T> void newGetRequest(String url, Class<T> clazz, Gson gson, RequestCallback<T> callback){

        GetRequest<T> request = new GetRequest<T>(url, clazz, gson, new RequestListener<T>(callback));
        Volley.newRequestQueue(context).add(request);
    }



}
