package com.android.ocasa.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.ocasa.model.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 15/02/16.
 */
public class Filter implements Parcelable{

    private List<ValidateCondition> conditions;

    public Filter() {
        this.conditions = new ArrayList<>();
    }

    public Filter(Parcel in) {
        conditions = new ArrayList<>();
        in.readTypedList(conditions, ValidateCondition.CREATOR);
    }

    public void addSimpleCondition(String value){
        conditions.add(new SimpleCondition(value));
    }

    public void addTimeCondition(String high, String low){
        conditions.add(new TimeCondition(high, low));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(conditions);
    }

    public Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

   /* public class DateCondition extends TimeCondition{

        public DateCondition(String high, String low) {
            super(high, low);
        }

        private DateCondition(Parcel in) {
            super(in);
        }

        @Override
        public boolean validate(Field field) {

            Date maxDate = DateTimeHelper.parseDate(high);
            Date minDate = DateTimeHelper.parseDate(low);

            Date value = DateTimeHelper.parseDate(field.getValue());

            if(maxDate.after(value) && minDate.before(value)){
                return true;
            }

            return false;
        }

        public Parcelable.Creator<DateCondition> CREATOR
                = new Parcelable.Creator<DateCondition>() {
            public DateCondition createFromParcel(Parcel in) {
                return new DateCondition(in);
            }

            public DateCondition[] newArray(int size) {
                return new DateCondition[size];
            }
        };
    }*/
}
