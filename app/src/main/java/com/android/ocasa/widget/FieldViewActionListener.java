package com.android.ocasa.widget;

import android.view.View;

/**
 * Created by ignacio on 28/01/16.
 */
public interface FieldViewActionListener {

    void onHistoryClick(View view);
    void onQrClick(View view);
    void onMapClick(FieldMapView view);
    void onDateClick(FieldDateView view);
    void onTimeClick(FieldTimeView view);
    void onPhoneClick(FieldPhoneView view);
    void onComboClick(FieldComboView view);
    void onListClick(FieldListView view);
    void onEditSignatureClick(FieldSignatureView view);
    void onEditPhotoClick(FieldPhotoView view);
}
