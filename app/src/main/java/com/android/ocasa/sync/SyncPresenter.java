package com.android.ocasa.sync;

import android.util.Log;

import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.service.OcasaService;
import com.codika.androidmvp.presenter.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 21/06/16.
 */
public class SyncPresenter extends BasePresenter<SyncView> {

    public SyncPresenter() {}

    public void sync(){
        OcasaService.getInstance().sync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpTable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Error", "Menu " + e.getMessage());
                        getView().onSyncFinish();
                    }

                    @Override
                    public void onNext(HttpTable menu) {
                        getView().onSyncFinish();
                    }
                });
    }

}
