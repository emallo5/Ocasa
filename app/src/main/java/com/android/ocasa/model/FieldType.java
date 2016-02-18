package com.android.ocasa.model;

import android.content.Context;
import android.location.Location;

import com.android.ocasa.R;
import com.android.ocasa.widget.factory.ComboFieldFactory;
import com.android.ocasa.widget.factory.DateFieldFactory;
import com.android.ocasa.widget.factory.DecimalFieldFactory;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.android.ocasa.widget.factory.IntegerFieldFactory;
import com.android.ocasa.widget.factory.MapFieldFactory;
import com.android.ocasa.widget.factory.PhoneFieldFactory;
import com.android.ocasa.widget.factory.TextFieldFactory;
import com.android.ocasa.widget.factory.TimeFieldFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 26/01/16.
 */
public enum FieldType {

    QR("qr"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TextFieldFactory();
        }
    },
    TEXT("text"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TextFieldFactory();
        }
    },
    MAP("map") {

        @Override
        public FieldViewFactory getFieldFactory() {
            return new MapFieldFactory();
        }

        @Override
        public String format(Object value) {

            LatLng location = (LatLng) value;

            String longitude = Location.convert(location.longitude, Location.FORMAT_DEGREES).replace(",", ".");
            String latitude = Location.convert(location.latitude, Location.FORMAT_DEGREES).replace(",", ".");

            return String.format("%s,%s",latitude,longitude);
        }

        @Override
        public Object parse(String value) {

            String[] values = value.split(",");

            return new LatLng(Double.valueOf(values[0]), Double.valueOf(values[1]));
        }

        @Override
        public String getDisplayText(Context context, String value) {

            String[] values = value.split(",");

            return context.getString(R.string.form_map_field_display_text, values[0], values[1]);
        }
    },
    PHONE("phone"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new PhoneFieldFactory();
        }
    },
    DOUBLE("decimal"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new DecimalFieldFactory();
        }
    },
    INTEGER("integer"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new IntegerFieldFactory();
        }
    },
    DATE("date"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new DateFieldFactory();
        }
    },
    TIME("time"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TimeFieldFactory();
        }
    },
    ATTACHMENT("attachment"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TextFieldFactory();
        }
    },
    COMBO("combo"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new ComboFieldFactory();
        }
    };

    private String apiName;

    FieldType(String value){
        this.apiName = value;
    }

    public String format(Object value){
        return value.toString();
    }

    public Object parse(String value){
        return value;
    }

    public String getDisplayText(Context context, String value){
        return value;
    }

    public FieldViewFactory getFieldFactory(){
        return null;
    }

    public static FieldType findTypeByApiName(String name){
        for (FieldType fieldType:FieldType.values()){
            if(fieldType.apiName.equalsIgnoreCase(name)){
                return fieldType;
            }
        }

        return null;
    }
}
