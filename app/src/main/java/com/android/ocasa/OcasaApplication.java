package com.android.ocasa;

import android.app.Application;

import com.crittercism.app.Crittercism;

/**
 * Created by ignacio on 11/01/16.
 */
public class OcasaApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Crittercism.initialize(this, getString(R.string.crittercism_appID));
    }
}
