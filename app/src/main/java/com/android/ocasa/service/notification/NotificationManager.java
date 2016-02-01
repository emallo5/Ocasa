package com.android.ocasa.service.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by ignacio on 21/01/16.
 */
public class NotificationManager {

    public static void sendBroadcast(Context context, String action){
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(action));
    }
}
