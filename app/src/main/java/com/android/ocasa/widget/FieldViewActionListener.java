package com.android.ocasa.widget;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 28/01/16.
 */
public interface FieldViewActionListener {

    public void onHistoryClick(int fieldId);
    public void onQrClick(int fieldId);
    public void onMapClick(FieldMapView view);
    public void onDateClick(FieldDateView view);
    public void onTimeClick(FieldTimeView view);
    public void onPhoneClick(FieldPhoneView view);
    public void onComboClick(FieldComboView view);
}
