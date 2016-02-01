package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.service.notification.NotificationManager;

/**
 * Created by ignacio on 21/01/16.
 */
public class UserService {

    public static final String USER_LOGIN_FINISHED_ACTION = "com.android.ocasa.service.UserService.USER_LOGIN_FINISHED_ACTION";

    private Context context;

    public UserService(Context context){
        this.context = context;
    }

    public void login(){
        NotificationManager.sendBroadcast(context, USER_LOGIN_FINISHED_ACTION);
    }
}
