package com.android.ocasa.widget;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 28/01/16.
 */
public interface FieldViewActionListener {

    public void onMapClick(FieldMapView view, LatLng location);
    public void onDateClick();
    public void onComboClick();
}
