package com.android.ocasa.widget;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.FieldType;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 28/01/16.
 */
public class FieldMapView extends LinearLayout implements FieldViewAdapter{

    private String value = "";

    private FieldViewActionListener listener;

    private TextView label;
    private EditText latitude;
    private EditText longitude;

    private ImageView map;

    public FieldMapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FieldMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.field_map_layout, this, true);

        label = (TextView) findViewById(R.id.label);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        map = (ImageView) findViewById(R.id.map);

        map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMapClick(FieldMapView.this);
            }
        });
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setLabel(String label) {
        this.label.setText(label);
    }

    public String getLabel(){
        return label.getText().toString();
    }

    @Override
    public void setValue(String value) {
        LatLng location = (LatLng) FieldType.MAP.parse(value);

        String lng = Location.convert(location.longitude, Location.FORMAT_DEGREES).replace(",", ".");
        String lat = Location.convert(location.latitude, Location.FORMAT_DEGREES).replace(",", ".");

        latitude.setText(lat);
        longitude.setText(lng);
    }

    @Override
    public String getValue() {

        String latitude = this.latitude.getText().toString();
        String longitude = this.longitude.getText().toString();

        if(latitude.isEmpty() || longitude.isEmpty())
            return "";

        return latitude.toString() + "," + longitude.toString();
    }
}
