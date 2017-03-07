package com.android.ocasa.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leandro on 7/3/17.
 */

public class LocationListByDay {

    private HashMap<String, ArrayList<Site>> locationList = new HashMap<>();

    public HashMap<String, ArrayList<Site>> getLocationList() {
        return locationList;
    }

    public void addLocationList(String day, ArrayList<Site> locationList) {
        this.locationList.put(day, locationList);
    }
}
