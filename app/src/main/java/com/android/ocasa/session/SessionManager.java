package com.android.ocasa.session;

import android.content.SharedPreferences;

/**
 * Created by ignacio on 14/07/16.
 */
public class SessionManager {

    static final String USER_ID = "_USER_";
    static final String DEVICE_ID = "_DEVICE_ID_";

    private static SessionManager instance;

    private SharedPreferences preferences;

    public void init(SharedPreferences preferences){
        this.preferences = preferences;
    }

    public static SessionManager getInstance() {
        if(instance == null)
            instance = new SessionManager();
        return instance;
    }

    public void saveUser(String user){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, user);
        editor.apply();
    }

    public String getUser(){
        return preferences.getString(USER_ID, "");
    }

    public void cleanSession(){
        preferences.edit().clear().apply();
    }

    public boolean isSigned() {
        return preferences.contains(USER_ID);
    }

    public void saveDeviceId(String deviceId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_ID, deviceId);
        editor.apply();
    }

    public String getDeviceId(){
        return preferences.getString(DEVICE_ID, "");
    }
}
