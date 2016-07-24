package com.android.ocasa.core;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.codika.androidmvp.fragment.BaseMvpFragment;
import com.codika.androidmvp.presenter.BasePresenter;
import com.codika.androidmvp.view.BaseView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by ignacio on 11/07/16.
 */
public abstract class LocationMvpFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpFragment<V, P> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient apiClient;

    private Location lastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        apiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        loadLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLocation();
            }
        }
    }

    private void loadLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public Location getLastLocation() {
        return lastLocation;
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
