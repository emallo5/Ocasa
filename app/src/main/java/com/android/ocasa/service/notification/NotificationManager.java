package com.android.ocasa.service.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by ignacio on 21/01/16.
 */
public class NotificationManager {

    public static void sendBroadcast(Context context, String action){
        sendBroadcast(context, new Intent(action));
    }

    public static void sendBroadcast(Context context, Intent intent){
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
