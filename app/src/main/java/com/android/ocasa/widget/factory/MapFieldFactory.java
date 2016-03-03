package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldMapView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 28/01/16.
 */
public class MapFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field, boolean isEditMode) {

        FieldMapView text = (FieldMapView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_map, container, false);

        text.setLabel(field.getColumn().getName());

        if(!isEditMode)
            text.setValue(field.getValue());

        return text;
    }

    @Override
    public View createSubView(ViewGroup container, Field field, boolean isEditMode) {
        return null;
    }
}
