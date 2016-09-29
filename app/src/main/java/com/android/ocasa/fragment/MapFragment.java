package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.core.fragment.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ignacio on 28/01/16.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {

    public static final String MAP_FIELD_TAG = "field_tag";

    public static final String DATA_NEW_LOCATION = "new_location";

    static final String ARG_LOCATION = "location";
    static final String ARG_TITLE = "title";

    private GoogleMap map;

    private CoordinatorLayout coordinatorLayout;
    private MapView mapView;

    private LatLng newLocation;

    public static MapFragment newInstance(String fieldTag, String title, LatLng location) {

        Bundle args = new Bundle();
        args.putString(MAP_FIELD_TAG, fieldTag);
        args.putString(ARG_TITLE, title);
        args.putParcelable(ARG_LOCATION, location);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getArguments().getString(ARG_TITLE));
    }

    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(view);
    }

    @Override
    public boolean getAllowReturnTransitionOverlap() {
        return super.getAllowReturnTransitionOverlap();
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.container);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng location = getArguments().getParcelable(ARG_LOCATION);

        this.map = map;

        if(location != null) {
            setNewLocation(location);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15), 2000, null);
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                setNewLocation(latLng);

                Snackbar.make(coordinatorLayout, R.string.map_new_location_text,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.map_new_location_button, MapFragment.this)
                        .show();
            }
        });

        Snackbar.make(coordinatorLayout, R.string.map_info_location_text,
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void setNewLocation(LatLng newLocation){
        this.newLocation = newLocation;

        map.clear();
        map.addMarker(new MarkerOptions().position(newLocation).title(getArguments().getString(ARG_TITLE)));    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(DATA_NEW_LOCATION, newLocation);
        intent.putExtra(MAP_FIELD_TAG, getArguments().getString(MAP_FIELD_TAG));

        getActivity().onBackPressed();

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
