package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by leandro on 7/6/17.
 */

public class TripData {

    @SerializedName("started_trip")
    private String startedTrip;
    @SerializedName("date")
    private Date date;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public TripData() {}

    public TripData(String startedTrip, Date date, double latitude, double longitude) {
        this.startedTrip = startedTrip;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartedTrip() {
        return startedTrip;
    }

    public void setStartedTrip(String startedTrip) {
        this.startedTrip = startedTrip;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
