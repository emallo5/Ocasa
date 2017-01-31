package com.android.ocasa.sync;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.core.LocationMvpActivity;
import com.android.ocasa.home.HomeActivity;
import com.android.ocasa.util.ConfigHelper;
import com.codika.androidmvp.activity.BaseMvpActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ignacio on 21/06/16.
 */
public class SyncActivity extends LocationMvpActivity<SyncView, SyncPresenter> implements SyncView{

    static final long FIVE_MINUTES = 1000 * 60 * 60;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sync);

        Uri uri = Uri.parse("asset:///animacion_loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();

        SimpleDraweeView progress = (SimpleDraweeView) findViewById(R.id.progress);
        progress.setController(controller);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().sync(0, 0);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Por favor espere...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationLoad(Location lastLocation) {
        super.onLocationLoad(lastLocation);
        getPresenter().sync(0, 0);
    }

    @Override
    public SyncView getMvpView() {
        return this;
    }

    @Override
    public Loader<SyncPresenter> getPresenterLoader() {
        return new SyncLoader(this);
    }

    @Override
    public void onSyncFinish() {
        setSyncAlarm();
        initSyncProcess();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void initSyncProcess() {
        if (!isMyServiceRunning(SyncIntentSerivce.class)) {
            Intent i = new Intent(SyncActivity.this, SyncIntentSerivce.class);
            startService(i);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setSyncAlarm(){

        Intent syncIntent = new Intent(this, SyncService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, syncIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIVE_MINUTES,
                FIVE_MINUTES,
                pendingIntent);
    }
}
