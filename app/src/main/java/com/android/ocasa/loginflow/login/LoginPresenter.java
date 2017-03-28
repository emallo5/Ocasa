package com.android.ocasa.loginflow.login;

import android.os.Build;
import android.util.Log;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.api.OcasaApiSubscriber;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.ApiError;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.util.ConfigHelper;
import com.codika.androidmvprx.presenter.BaseRxPresenter;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;

/**
 * Created by ignacio on 27/07/16.
 */
public class LoginPresenter extends BaseRxPresenter<LoginView> {

    static final String TAG = "LoginPresenter";

    private Subscription subscription;

    private AsyncSubject<Menu> subject;

    public LoginPresenter(){
        subject = AsyncSubject.create();
    }

    private boolean isEmptyFields(String email, String password){
        return email.trim().length() == 0 || password.trim().length() == 0;
    }

    private boolean isValidEmail(String email){
        return email.trim().length() > 0;
    }

    public void login(String email, String password, String deviceId){

        if(isEmptyFields(email, password)){
            getView().showEmptyFieldsAlert();
            return;
        }

        if(!isValidEmail(email)){
            getView().showInvalidEmail();
            return;
        }

        LoginCredentials credentials = new LoginCredentials();
        credentials.setUser(email);
        credentials.setPassword(password);
        credentials.setImei(deviceId);
        credentials.setAndroidVersion(String.valueOf(android.os.Build.VERSION.SDK_INT));
        credentials.setAppVersion(BuildConfig.VERSION_NAME);

        SessionManager.getInstance().saveDeviceId(deviceId);

        getView().showProgress();

        login(credentials);
    }

    private void login(LoginCredentials credentials){

        subscription = OcasaService.getInstance()
                .login(credentials)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);
    }

    @Override
    public void onAttachView(LoginView view) {
        super.onAttachView(view);
        connectSubject();
    }

    private void connectSubject(){
        addSubscription(subject.asObservable()
                .subscribe(new LoginSubscriber()));
    }

    private void reconnectSubject(){
        subject = AsyncSubject.create();
        connectSubject();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null)
            subscription.unsubscribe();
    }

    private class LoginSubscriber extends OcasaApiSubscriber<Menu> {

        @Override
        public void onCompleted() {
            reconnectSubject();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
//            Log.v(TAG, e.getMessage());
            reconnectSubject();
        }

        @Override
        public void onHttpError(ApiError error) {
            getView().onLoginError(error.getDescription());
        }

        @Override
        public void onNetworkError() {
            getView().onNetworkError();
        }

        @Override
        public void onNext(Menu menu) {
            getView().onLoginSuccess();
            ConfigHelper.getInstance().setAppConfiguration(menu.getConfiguration());
            SessionManager.getInstance().saveUser("");
        }
    }
}
