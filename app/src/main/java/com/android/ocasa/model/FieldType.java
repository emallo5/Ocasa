package com.android.ocasa.model;

import android.content.Context;
import android.location.Location;

import com.android.ocasa.R;
import com.android.ocasa.widget.factory.ComboFieldFactory;
import com.android.ocasa.widget.factory.DateFieldFactory;
import com.android.ocasa.widget.factory.DecimalFieldFactory;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.android.ocasa.widget.factory.IntegerFieldFactory;
import com.android.ocasa.widget.factory.ListFieldFactory;
import com.android.ocasa.widget.factory.MapFieldFactory;
import com.android.ocasa.widget.factory.PhoneFieldFactory;
import com.android.ocasa.widget.factory.TextFieldFactory;
import com.android.ocasa.widget.factory.TimeFieldFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Pattern;

/**
 * Created by ignacio on 26/01/16.
 */
public enum FieldType {

    QR("qr"),
    TEXT("text"),
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

            if(values.length == 1){
                return "";
            }

            return context.getString(R.string.form_map_field_display_text, values[0], values[1]);
        }
    },
    PHONE("phone"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new PhoneFieldFactory();
        }

        @Override
        public boolean checkValue(String value) {

            String phonePattern = "\\d";

            Pattern pattern = Pattern.compile(phonePattern);

            return !pattern.matcher(value).matches();
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

        @Override
        public boolean checkValue(String value) {

            String timePattern = "((?:19|20)\\d\\d)/(0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])";

            Pattern pattern = Pattern.compile(timePattern);

            return !pattern.matcher(value).matches();
        }

    },
    TIME("time"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TimeFieldFactory();
        }

        @Override
        public boolean checkValue(String value) {

            String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

            Pattern pattern = Pattern.compile(timePattern);

            return !pattern.matcher(value).matches();
        }
    },
    ATTACHMENT("attachment"),
    COMBO("combo"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new ComboFieldFactory();
        }
    }, LIST("list"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new ListFieldFactory();
        }
    };

    private String apiName;

    FieldType(String value){
        this.apiName = value;
    }

    public boolean checkValue(String value){
        return true;
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
        return new TextFieldFactory();
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
