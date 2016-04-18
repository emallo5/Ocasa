package com.android.ocasa;

import android.app.Application;

import com.crittercism.app.Crittercism;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;

/**
 * Created by ignacio on 11/01/16.
 */
public class OcasaApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        Crittercism.initialize(this, getString(R.string.crittercism_appID));

        DualCacheContextUtils.setContext(this);
    }
}
