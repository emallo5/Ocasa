package com.android.ocasa.model;

import com.android.ocasa.util.DateTimeHelper;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by leandro on 7/6/17.
 */

public class TripData {

    @SerializedName("action")
    private String startedTrip;
    @SerializedName("date")
    private String date;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public TripData() {}

    public TripData(String startedTrip, Date date, String latitude, String longitude) {
        this.startedTrip = startedTrip;
        this.date = DateTimeHelper.formatDateTime(date);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartedTrip() {
        return startedTrip;
    }

    public void setStartedTrip(String startedTrip) {
        this.startedTrip = startedTrip;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
