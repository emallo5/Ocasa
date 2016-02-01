package com.android.ocasa.apiservice;

import android.content.Context;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.CustomTestRunner;
import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.Application;
import com.android.ocasa.service.MenuService;
import com.android.volley.VolleyError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by ignacio on 21/01/16.
 */
@RunWith(CustomTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MenuServiceTest {

    @Mock
    private MenuService menuService;

    @Captor
    private ArgumentCaptor<RequestCallback<Menu>> dummyCallbackArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void syncMenuHttp200() {

        Context context = RuntimeEnvironment.application.getApplicationContext();

        Menu testMenu = new Menu();

        List<Application> applications = new ArrayList<>();

        Application application = new Application();
        application.setId("1");
        application.setName("Test");

        applications.add(application);

        testMenu.setApplications(applications);

        final Menu menu = new Menu();

        MenuService service = new MenuService(context);
        service.downloadMenu(new RequestCallback<Menu>() {
            @Override
            public void onSuccess(Menu response) {
                menu.setApplications(response.getApplications());
            }

            @Override
            public void onError(VolleyError error) {
            }
        });

        verify(menuService).syncMenu(dummyCallbackArgumentCaptor.capture());

        dummyCallbackArgumentCaptor.getValue().onSuccess(testMenu);

        assertThat(menu, is(equalTo(testMenu)));
    }


}
