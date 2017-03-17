package com.android.ocasa.sync;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.UploadLog;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.service.ReceiptService;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.ConnectionUtil;
import com.android.ocasa.util.Constants;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ignacio on 24/01/17.
 */

public class SyncIntentSerivce extends Service {

    public static final String TAG = "intentService";
    public static final int MINUTES_DELAY = 2;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    ScheduledExecutorService scheduler;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate (new Runnable() {
                public void run() {
                    syncData();
                }
            }, 0, MINUTES_DELAY, TimeUnit.MINUTES);
        }
    }

    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "StartingService");

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void syncData() {

        if (!ConnectionUtil.isInternetAvailable(getApplicationContext())) return;

        Log.d(TAG, "Service Process");

        final List<Receipt> opens = new ReceiptService().getOpenReceipts(getApplicationContext());
        final int count = opens.size();

        if (count != 0) {
            OcasaService.getInstance()
                    .uploadReceipts(opens)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "Sync completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.d(TAG, "Sync fail");
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            Log.d(TAG, "Sync succes");
                        }
                    });
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "SyncService removed");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "SyncService finished");
        super.onDestroy();
    }
}
