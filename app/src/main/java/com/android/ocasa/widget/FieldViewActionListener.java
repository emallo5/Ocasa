package com.android.ocasa.widget;

import android.view.View;

/**
 * Created by ignacio on 28/01/16.
 */
public interface FieldViewActionListener {

    public void onHistoryClick(View view);
    public void onQrClick(View view);
    public void onMapClick(FieldMapView view);
    public void onDateClick(FieldDateView view);
    public void onTimeClick(FieldTimeView view);
    public void onPhoneClick(FieldPhoneView view);
    public void onComboClick(FieldComboView view);
    public void onListClick(FieldListView view);
}
