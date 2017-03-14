package com.android.ocasa.sync;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.ocasa.model.LocationModel;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.FileHelper;

import java.util.Date;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by leandro on 2/3/17.
 */

public class LocationLogService extends Service {

    private static final String TAG = "LocationService";
    private static final int LOCATION_TIME_LAP = 10000;

    LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("LocationService", "Starting");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(LocationLogService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                out();
            } else {
                if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_TIME_LAP, 10, locationNETListener);

                if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_TIME_LAP, 10, locationGPSListener);
            }
        } else
            out();

        return START_STICKY;
    }

    private void out () {

        if (ContextCompat.checkSelfPermission(LocationLogService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                locationManager.removeUpdates(locationGPSListener);
            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                locationManager.removeUpdates(locationNETListener);
        }
        stopSelf();
    }

    LocationListener locationGPSListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            sendResult(location);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {

        }
    };

    LocationListener locationNETListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            sendResult(location);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };

    private void sendResult (Location location) {
        String date = DateTimeHelper.formatDate(new Date());
        String time = DateTimeHelper.formatTime(new Date());
        String imei = SessionManager.getInstance().getDeviceId();

        FileHelper.getInstance().saveLocation(location.getLatitude() + " " + location.getLongitude() + " " + FileHelper.IS_NOT_POD + ".");

        LocationModel data = new LocationModel(imei, date, time, location.getLatitude(), location.getLongitude());

        OcasaService.getInstance().sendLocationData(data).retry(2).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "Location removed");
        out();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Location finished");
        out();
        super.onDestroy();
    }
}
