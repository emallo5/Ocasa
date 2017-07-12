package com.android.ocasa.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.ocasa.model.AppConfiguration;
import com.android.ocasa.model.PodStructuresById;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KeyName, KeyValue);
            editor.commit();
        } catch (Exception e) {

        } catch (Error x) {

        }
    }

    public void WriteConfigBoolean(String KeyName, boolean KeyValue) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KeyName, KeyValue);
            editor.commit();
        } catch (Exception e) {

        } catch (Error x) {

        }
    }

    public boolean ReadConfigBoolean(String KeyName, boolean defaultValue) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getBoolean(KeyName, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        } catch (Error x) {
            return defaultValue;
        }
    }

    public void WriteObjectConfig(String KeyName, Object KeyValue) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KeyName, new Gson().toJson(KeyValue));
            editor.commit();
        } catch (Exception e) {

        } catch (Error x) {

        }
    }

    public Object ReadObjectConfig(String KeyName, Type KeyValue) {

        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String v = preferences.getString(KeyName, "");

            if (v.isEmpty()) return null;
            return new Gson().fromJson(v, KeyValue);

        } catch (Exception e) {
            return null;
        } catch (Error x) {
            return null;
        }

    }

    public AppConfiguration getAppConfiguration() {
        AppConfiguration config = (AppConfiguration) ReadObjectConfig("app_configuration", AppConfiguration.class);
        return config == null ? new AppConfiguration() : config;
    }

    public void setAppConfiguration(AppConfiguration appConfiguration) {
        WriteObjectConfig("app_configuration", appConfiguration);
    }

    public List<PodStructuresById> getPodStructureByIdList() {
        Type listType = new TypeToken<List<PodStructuresById>>(){}.getType();
        List<PodStructuresById> structures = (List<PodStructuresById>) ReadObjectConfig("pod_structure_by_id", listType);
        return structures != null ? structures : new ArrayList<PodStructuresById>();
    }

    public void setPodStructureById(List<PodStructuresById> structureById) {
//        Type listType = new TypeToken<List<PodStructuresById>>(){}.getType();
        WriteObjectConfig("pod_structure_by_id", structureById);
    }
}
