package com.android.ocasa.map;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.android.ocasa.R;
import com.android.ocasa.model.LocationListByDay;
import com.android.ocasa.model.Site;
import com.android.ocasa.util.FileHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Spinner spinner;
    private CheckBox checkBox;
    private LocationListByDay sites;
    private GoogleMap mMap;
    private boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner) findViewById(R.id.spinnerDay);
        checkBox = (CheckBox) findViewById(R.id.cb_draw_trip);

        initSpinner();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34.604417, -58.392312), 10));
    }

    private void initSpinner() {
        sites = FileHelper.getInstance().readLocation();
        List<String> spinnerArray = new ArrayList<>();

        for (String day : sites.getLocationList().keySet())
            spinnerArray.add(day);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String day = (String) spinner.getAdapter().getItem(position);
                if (!mapReady) return;

                mMap.clear();

                if (checkBox.isChecked()) {
                    drawTrip();
                } else {
                    drawPoints();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    drawTrip();
                else
                    drawPoints();
            }
        });
    }

    private void drawTrip() {
        mMap.clear();
        ArrayList<Site> points = sites.getLocationList().get(spinner.getSelectedItem());
        PolylineOptions options = new PolylineOptions().width(7).color(Color.RED).geodesic(true);
        for (Site site : points) {
            LatLng loc = new LatLng(site.lat, site.lng);
            options.add(loc);
        }

        mMap.addMarker(new MarkerOptions().position(new LatLng(points.get(0).lat, points.get(0).lng)).title(points.get(0).date));
        mMap.addMarker(new MarkerOptions().position(new LatLng(points.get(points.size()-1).lat, points.get(points.size()-1).lng)).title(points.get(points.size()-1).date));
        mMap.addPolyline(options);
    }

    private void drawPoints() {
        mMap.clear();
        ArrayList<Site> points = sites.getLocationList().get(spinner.getSelectedItem());
        for (Site site : points) {
            LatLng loc = new LatLng(site.lat, site.lng);
            mMap.addMarker(new MarkerOptions().position(loc).title(site.date));
        }
    }
}
