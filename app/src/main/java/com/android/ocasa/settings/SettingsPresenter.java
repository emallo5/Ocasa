package com.android.ocasa.settings;

import com.android.ocasa.model.Layout;
import com.android.ocasa.service.OcasaService;
import com.codika.androidmvprx.presenter.BaseRxPresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 10/11/16.
 */

public class SettingsPresenter extends BaseRxPresenter<SettingsView> {


    public void logout(){
        OcasaService.getInstance()
                .logout()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                        getView().onLogoutSuccess();
                    }
                });
    }

    public void sync(){

        OcasaService.getInstance()
                .sync(0, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Layout>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Layout layout) {
                        getView().onSyncSuccess();
                    }
                });
    }

}
