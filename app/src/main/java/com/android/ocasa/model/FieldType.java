package com.android.ocasa.model;

import com.android.ocasa.widget.factory.DateFieldFactory;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.android.ocasa.widget.factory.MapFieldFactory;
import com.android.ocasa.widget.factory.TextFieldFactory;
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
        public Object format(String value) {

            String[] values = value.split(",");

            return new LatLng(Double.valueOf(values[0]), Double.valueOf(values[1]));
        }
    },
    PHONE("phone"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TextFieldFactory();
        }
    },
    DOUBLE("double"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TextFieldFactory();
        }
    },
    INTEGER("integer"){
        @Override
        public FieldViewFactory getFieldFactory() {
            return new TextFieldFactory();
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
            return new TextFieldFactory();
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
            return new TextFieldFactory();
        }
    };

    private String apiName;

    FieldType(String value){
        this.apiName = value;
    }

    public Object format(String value){
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
