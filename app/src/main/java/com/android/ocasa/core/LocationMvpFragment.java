package com.android.ocasa.core;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.android.ocasa.OcasaApplication;
import com.android.ocasa.util.FileHelper;
import com.codika.androidmvp.fragment.BaseMvpFragment;
import com.codika.androidmvp.presenter.BasePresenter;
import com.codika.androidmvp.view.BaseView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

/**
 * Created by ignacio on 11/07/16.
 */
public abstract class LocationMvpFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpFragment<V, P>
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String GPS_TAG = "GPS debug";
    private static final double PERMISSION_DENIED = 1;
    private static final double CONNECTION_FAILED = 2;
    private static final double LOCATION_DISABLED = 3;

    protected boolean AVAILABLE_GPS_FUNCTION = false;

    private GoogleApiClient apiClient;
    private LocationRequest mLocationRequest;

    private Location lastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AVAILABLE_GPS_FUNCTION)
            buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AVAILABLE_GPS_FUNCTION) {
            FileHelper.getInstance().writeToFile("GPS:connecting");
            apiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiClient == null) return;

        if (apiClient.isConnected()) {
            FileHelper.getInstance().writeToFile("GPS:desconnecting");
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
            apiClient.disconnect();
        }

        if (apiClient.isConnectionCallbacksRegistered(this))
            apiClient.unregisterConnectionFailedListener(this);
        if (apiClient.isConnectionFailedListenerRegistered(this))
            apiClient.unregisterConnectionCallbacks(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        FileHelper.getInstance().writeToFile("GPS:connected");
        loadLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1)
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                loadLocation();
    }

    private void loadLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            FileHelper.getInstance().writeToFile("GPS:creatingReq");
            createLocationRequest();

            if (LocationServices.FusedLocationApi.getLocationAvailability(apiClient).isLocationAvailable()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
                FileHelper.getInstance().writeToFile("GPS:receivingUpdates");
            }
            else {
                loadDummyLocation(LOCATION_DISABLED);
                FileHelper.getInstance().writeToFile("GPS:notAvailable");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        Log.d(GPS_TAG, "location changed");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
        FileHelper.getInstance().writeToFile("GPS:suspended-reconecting");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loadDummyLocation(CONNECTION_FAILED);
        FileHelper.getInstance().writeToFile("GPS:connect-failed-retrying");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiClient.connect();
            }
        }, 2000);
    }

    public Location getLastLocation() {
        if (lastLocation == null) {
            loadDummyLocation(PERMISSION_DENIED);
            FileHelper.getInstance().writeToFile("GPS:locationRetNull");
        }

        FileHelper.getInstance().writeToFile("GPS:returnedSucc");
        return lastLocation;
    }

    private void loadDummyLocation(double code) {
        lastLocation = new Location("dummyprovider");
        lastLocation.setLatitude(code);
        lastLocation.setLongitude(code);
    }

    public void showFragment(String hideTag, Fragment fragment, String shoTag){

        if(getFragmentManager().findFragmentByTag(shoTag) != null)
            return;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.android.ocasa.core.R.anim.slide_in_left,
                com.android.ocasa.core.R.anim.slide_out_left,
                com.android.ocasa.core.R.anim.slide_in_right,
                com.android.ocasa.core.R.anim.slide_out_right);
        transaction.hide(getFragmentManager().findFragmentByTag(hideTag));
        transaction.add(com.android.ocasa.core.R.id.container, fragment, shoTag);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void showDialog(String tag, DialogFragment dialog){

        if(getChildFragmentManager().findFragmentByTag(tag) != null)
            return;

        dialog.show(getChildFragmentManager(), tag);
    }

}
