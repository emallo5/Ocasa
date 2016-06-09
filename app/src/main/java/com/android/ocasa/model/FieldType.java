package com.android.ocasa.model;

import com.android.ocasa.widget.factory.ComboFieldFactory;
import com.android.ocasa.widget.factory.DateFieldFactory;
import com.android.ocasa.widget.factory.DateTimeFieldFactory;
import com.android.ocasa.widget.factory.DecimalFieldFactory;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.android.ocasa.widget.factory.IntegerFieldFactory;
import com.android.ocasa.widget.factory.ListFieldFactory;
import com.android.ocasa.widget.factory.MapFieldFactory;
import com.android.ocasa.widget.factory.PhoneFieldFactory;
import com.android.ocasa.widget.factory.TextFieldFactory;
import com.android.ocasa.widget.factory.TimeFieldFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
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

            DecimalFormat df = new DecimalFormat("##.######", new DecimalFormatSymbols(Locale.US));

            String longitude = df.format(location.longitude);
            String latitude = df.format(location.latitude);

            return String.format("%s,%s",latitude,longitude);
        }

        @Override
        public Object parse(String value) {

            String[] values = value.split(",");

            return new LatLng(Double.valueOf(values[0]), Double.valueOf(values[1]));
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

        @Override
        public boolean isValidValue(String value) {

            String timePattern = "((?:19|20)\\d\\d)/(0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])";

            Pattern pattern = Pattern.compile(timePattern);

            return pattern.matcher(value).matches();
        }

    },
    TIME("time"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TimeFieldFactory();
        }

        @Override
        public boolean isValidValue(String value) {

            String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

            Pattern pattern = Pattern.compile(timePattern);

            return pattern.matcher(value).matches();
        }
    },
    DATETIME("datetime"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new DateTimeFieldFactory();
        }

        @Override
        public boolean isValidValue(String value) {

            String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

            Pattern pattern = Pattern.compile(timePattern);

            return pattern.matcher(value).matches();
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

    public boolean isValidValue(String value){
        return true;
    }

    public String format(Object value){
        return value.toString();
    }

    public Object parse(String value){
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
