package com.android.ocasa;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.ocasa.api.OcasaApi;
import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.session.SessionManager;
import com.crittercism.app.Crittercism;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ignacio on 11/01/16.
 */
public class OcasaApplication extends Application{

    private OcasaApi api;

    @Override
    public void onCreate() {
        super.onCreate();

        api = provideFiviApi();

        SessionManager.getInstance().init(getSharedPreferences("Session", MODE_PRIVATE));
        OcasaService.getInstance().init(this, api);

        Fresco.initialize(this);

        Crittercism.initialize(this, getString(R.string.crittercism_appID));

        DualCacheContextUtils.setContext(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public OcasaApi getApi() {
        return api;
    }

    private OcasaApi provideFiviApi() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG)
            httpClient.interceptors().add(logging);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Action.class, new Menu.ActionDeserializer())
                .registerTypeAdapter(Record.class, new TableRecord.RecordDeserializer())
                .registerTypeAdapter(Column.class, new HttpTable.ColumnDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(OcasaApi.class);
    }
}
