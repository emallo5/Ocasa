package com.android.ocasa.sync;

import android.app.IntentService;
import android.content.Intent;

import com.android.ocasa.service.UserService;

/**
 * Created by ignacio on 21/01/16.
 */
public class LoginService extends IntentService {

    public LoginService() {
        super("LoginService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new UserService(this).login();
    }
}
