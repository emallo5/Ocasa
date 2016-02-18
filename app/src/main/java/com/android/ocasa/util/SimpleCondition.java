package com.android.ocasa.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.ocasa.model.Field;

/**
 * Created by ignacio on 18/02/16.
 */
public class SimpleCondition extends ValidateCondition {

    private String value;

    public SimpleCondition(String value) {
        this.value = value;
    }

    public SimpleCondition(Parcel in) {
        value = in.readString();
    }

    @Override
    public boolean validate(Field field) {
        return field.getValue().contains(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ValidateCondition.CLASS_TYPE_SIMPLE);
        super.writeToParcel(parcel, i);
        parcel.writeString(value);
    }

    public Parcelable.Creator<SimpleCondition> CREATOR = new Parcelable.Creator<SimpleCondition>() {
        public SimpleCondition createFromParcel(Parcel in) {
            return new SimpleCondition(in);
        }

        public SimpleCondition[] newArray(int size) {
            return new SimpleCondition[size];
        }
    };
}
