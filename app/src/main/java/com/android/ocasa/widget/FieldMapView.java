package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 28/01/16.
 */
public class FieldMapView extends TextView implements FieldViewAdapter{

    private FieldViewActionListener listener;

    private LatLng location;

    public FieldMapView(Context context, final LatLng location) {
        super(context);

        init();
    }

    public FieldMapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FieldMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMapClick(FieldMapView.this, location);
            }
        });
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
