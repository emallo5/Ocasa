package com.android.ocasa.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.ocasa.model.Field;

/**
 * Created by ignacio on 18/02/16.
 */
public abstract class ValidateCondition implements Parcelable {

    static final int CLASS_TYPE_SIMPLE = 1;
    static final int CLASS_TYPE_TIME = 2;
    static final int CLASS_TYPE_DATE = 3;

    public abstract boolean validate(Field field);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Creator<ValidateCondition> CREATOR = new Creator<ValidateCondition>() {
        @Override
        public ValidateCondition createFromParcel(Parcel source) {

            return getCondition(source);
        }

        @Override
        public ValidateCondition[] newArray(int size) {
            return new ValidateCondition[size];
        }
    };

    public static ValidateCondition getCondition(Parcel source) {

        switch (source.readInt()) {
            case CLASS_TYPE_SIMPLE:
                return new SimpleCondition(source);
            case CLASS_TYPE_TIME:
                return new TimeCondition(source);
            default:
                return null;
        }
    }

}
