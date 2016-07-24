package com.android.ocasa;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.service.MenuService;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@LargeTest
public class ApplicationTest extends ApplicationTestCase<Application> {

    CountDownLatch signal = null;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        signal.countDown();
    }

    public void testAlbumGetTask() throws InterruptedException {


    }
}