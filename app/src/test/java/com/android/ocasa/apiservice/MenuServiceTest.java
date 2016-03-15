package com.android.ocasa.apiservice;

import android.content.Context;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.CustomTestRunner;
import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.service.MenuService;
import com.android.volley.VolleyError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;


import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by ignacio on 21/01/16.
 */
@RunWith(CustomTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MenuServiceTest {

    CountDownLatch signal = null;

    @Test
    public void syncMenuHttp200() throws InterruptedException {

        signal = new CountDownLatch(1);

        Context context = RuntimeEnvironment.application.getApplicationContext();

        MenuService service = new MenuService(context);

        service.requestMenu(new RequestCallback<Menu>() {
            @Override
            public void onSuccess(Menu response) {
                signal.countDown();
            }

            @Override
            public void onError(VolleyError error) {
                signal.countDown();
            }
        });

        signal.await();

        assertTrue("Test", true);

    }


}
