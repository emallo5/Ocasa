package com.android.ocasa.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by ignacio on 10/01/17.
 */

public class ConnectionUtil {

    public static boolean isInternetAvailable (Context context) {
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        } catch (Exception e) {
            return false;
        }
    }

}
