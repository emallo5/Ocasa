package com.android.ocasa.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.ocasa.model.Field;

import java.util.Date;

/**
 * Created by ignacio on 18/02/16.
 */
public class TimeCondition extends ValidateCondition {

    protected String high;
    protected String low;

    public TimeCondition(String high, String low) {
        this.high = high;
        this.low = low;
    }

    public TimeCondition(Parcel in) {
        high = in.readString();
        low = in.readString();
    }

    @Override
    public boolean validate(Field field) {

        Date maxDate = DateTimeHelper.parseTime(high);
        Date minDate = DateTimeHelper.parseTime(low);

        Date value = DateTimeHelper.parseTime(field.getValue());

        if(maxDate.after(value) && minDate.before(value)){
            return true;
        }

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(high);
        parcel.writeString(low);
    }

    public Parcelable.Creator<TimeCondition> CREATOR
            = new Parcelable.Creator<TimeCondition>() {
        public TimeCondition createFromParcel(Parcel in) {
            return new TimeCondition(in);
        }

        public TimeCondition[] newArray(int size) {
            return new TimeCondition[size];
        }
    };
}
