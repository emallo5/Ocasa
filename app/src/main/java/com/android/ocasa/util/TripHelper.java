package com.android.ocasa.util;

import com.android.ocasa.model.TripData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by leandro on 12/6/17.
 */

public class TripHelper {

    private static final String TRIP_DATA = "trip_data";
    private static final String TRIP_MOVEMENT = "trip_movement";

    public static void registerTripMovement(TripData tripData) {
        List<TripData> trips;

        String data = ConfigHelper.getInstance().ReadConfig(TRIP_DATA);
        if (data.isEmpty())
            trips = new ArrayList<>();
        else
            trips = new Gson().fromJson(data, new TypeToken<List<TripData>>(){}.getType());

        trips.add(tripData);

        ConfigHelper.getInstance().WriteConfig(TRIP_DATA, new Gson().toJson(trips));
    }

    public static String getTripMovementsString() {
        return ConfigHelper.getInstance().ReadConfig(TRIP_DATA);
    }

    public static void clearTripMovements() {
        ConfigHelper.getInstance().WriteConfig(TRIP_DATA, "");
    }

    public static String getLastMovement() {
        String data = ConfigHelper.getInstance().ReadConfig(TRIP_MOVEMENT);
        if (data.isEmpty()) return "F";

        if (data.split("-")[0].equals("F")) return "F";

        String day = data.split("-")[1];
        if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) > Integer.valueOf(day))
            return "F";
        else return "I";
    }

    public static void setLastMovement(boolean started) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        String data = started ? "I" : "F";
        ConfigHelper.getInstance().WriteConfig(TRIP_MOVEMENT, data + "-" + day);
    }
}
