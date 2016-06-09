package com.android.ocasa.widget;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.util.IconizedMenu;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 28/01/16.
 */
public class FieldMapView extends LinearLayout implements FieldViewAdapter, IconizedMenu.OnMenuItemClickListener{

    private String value = "";

    private FieldViewActionListener listener;

    private TextView label;
    private EditText latitude;
    private EditText longitude;

    private ImageView action;

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
        action = (ImageView) findViewById(R.id.map);

        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
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
    public void changeLabelVisbility(boolean visibility) {

    }

    @Override
    public String getValue() {

        String latitude = this.latitude.getText().toString();
        String longitude = this.longitude.getText().toString();

        if(latitude.isEmpty() || longitude.isEmpty())
            return "";

        return latitude.toString() + "," + longitude.toString();
    }

    private void showMenu(){
        IconizedMenu actionMenu = new IconizedMenu(getContext(), action);
        actionMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = actionMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_form_map_field, actionMenu.getMenu());
        actionMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.location:
                listener.onMapClick(FieldMapView.this);
                break;
            case R.id.history:
                listener.onHistoryClick(this);
                break;
        }

        return true;
    }
}
