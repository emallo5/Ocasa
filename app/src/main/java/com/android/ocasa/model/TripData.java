package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by leandro on 7/6/17.
 */

public class TripData {

    @SerializedName("started_trip")
    private boolean startedTrip;
    @SerializedName("date")
    private Date date;

    public TripData() {}

    public TripData(boolean startedTrip, Date date) {
        this.startedTrip = startedTrip;
        this.date = date;
    }

    public boolean isStartedTrip() {
        return startedTrip;
    }

    public void setStartedTrip(boolean startedTrip) {
        this.startedTrip = startedTrip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
