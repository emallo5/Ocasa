package com.android.ocasa.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by ignacio on 24/01/17.
 */

public class ConfigHelper {

    private static ConfigHelper instance;
    private Context context;
    private final String TAG = ConfigHelper.class.getName();

    public static ConfigHelper getInstance() {
        if (instance == null) {
            instance = new ConfigHelper();
        }
        return instance;
    }

    public void init (Context context) {
        this.context = context;
    }

    public String ReadConfig(String KeyName) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getString(KeyName, "");
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        } catch (Error x) {
            return "";
        }
    }

    public void WriteConfig(String KeyName, String KeyValue) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KeyName, KeyValue);
            editor.commit();
        } catch (Exception e) {

        } catch (Error x) {

        }
    }

    public void WriteConfigBoolean(String KeyName, boolean KeyValue) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KeyName, KeyValue);
            editor.commit();
        } catch (Exception e) {

        } catch (Error x) {

        }
    }

    public boolean ReadConfigBoolean(String KeyName, boolean defaultValue) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return preferences.getBoolean(KeyName, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        } catch (Error x) {
            return defaultValue;
        }
    }

    public void WriteObjectConfig(String KeyName, Object KeyValue) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KeyName, new Gson().toJson(KeyValue));
            editor.commit();
        } catch (Exception e) {

        } catch (Error x) {

        }
    }


    public Object ReadObjectConfig(String KeyName, Class KeyValue) {

        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);

            return new Gson().fromJson(preferences.getString(KeyName, ""), KeyValue);

        } catch (Exception e) {
            return null;
        } catch (Error x) {
            return null;
        }

    }
}
