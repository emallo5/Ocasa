package com.android.ocasa.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by ignacio on 24/01/17.
 */

public class ConfigHelper {

    private final String TAG = ConfigHelper.class.getName();

    public static String ReadConfig(Context context, String KeyName) {
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

    public static void WriteConfig(Context context, String KeyName, String KeyValue) {
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

    public static void WriteConfigBoolean(Context context, String KeyName, boolean KeyValue) {
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

    public static boolean ReadConfigBoolean(Context context, String KeyName, boolean defaultValue) {
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

    public static void WriteObjectConfig(Context context, String KeyName, Object KeyValue) {
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


    public static Object ReadObjectConfig(Context context, String KeyName, Class KeyValue) {

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
