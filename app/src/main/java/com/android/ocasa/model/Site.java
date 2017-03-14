package com.android.ocasa.model;

/**
 * Created by leandro on 7/3/17.
 */

public class Site {

    public String date;
    public double lat;
    public double lng;
    public boolean isPod;

    public Site(String date, double lat, double lng, boolean isPod) {
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.isPod = isPod;
    }

}
