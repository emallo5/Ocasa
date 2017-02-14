package com.android.ocasa.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.Constants;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 27/07/16.
 */
public class SyncService extends Service {

    static final String TAG = "SyncService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(TAG, "Start syncing");

//        OcasaService.getInstance()
//                .sync(0, 0)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<Layout>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        stopSelf();
//                    }
//
//                    @Override
//                    public void onNext(Layout httpTable) {
//                        stopSelf();
//                    }
//                });

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
