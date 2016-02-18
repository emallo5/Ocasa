package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.util.AlertDialogFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ignacio on 28/01/16.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, AlertDialogFragment.OnAlertClickListener {

    public static final String MAP_FIELD_TAG = "field_tag";

    public static final String DATA_NEW_LOCATION = "new_location";

    static final String ARG_LOCATION = "location";

    private MapView mapView;

    private LatLng newLocation;

    public static MapFragment newInstance(String fieldTag, LatLng location) {

        Bundle args = new Bundle();
        args.putString(MAP_FIELD_TAG, fieldTag);
        args.putParcelable(ARG_LOCATION, location);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        final LatLng location = getArguments().getParcelable(ARG_LOCATION);

        map.addMarker(new MarkerOptions().position(location).title("Marker"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15), 2000, null);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                newLocation = latLng;
                AlertDialogFragment alert = AlertDialogFragment.newInstance("Nueva ubicación", "¿Esta seguro de establecer esta nueva ubicación?");
                alert.show(getChildFragmentManager(), "Alert");
            }
        });
    }

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
    public void onPosiviteClick() {

        Intent intent = new Intent();
        intent.putExtra(DATA_NEW_LOCATION, newLocation);
        intent.putExtra(MAP_FIELD_TAG, getArguments().getString(MAP_FIELD_TAG));

        getActivity().onBackPressed();

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @Override
    public void onNegativeClick() {

    }
}
