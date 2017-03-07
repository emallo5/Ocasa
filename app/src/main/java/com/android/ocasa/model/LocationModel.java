package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leandro on 2/3/17.
 */

public class LocationModel {

    @SerializedName("imei")
    private String imei;

    @SerializedName("date")
    private String date;

    @SerializedName("timer")
    private String time;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;


    public LocationModel() {
    }

    public LocationModel(String imei, String date, String time, double latitude, double longitude) {
        this.imei = imei;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
