package com.android.ocasa.sync;

import android.app.IntentService;
import android.content.Intent;

import com.android.ocasa.service.UserService;

/**
 * Created by ignacio on 11/02/16.
 */
public class SendService extends IntentService {

    public SendService() {
        super("SendService");
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
