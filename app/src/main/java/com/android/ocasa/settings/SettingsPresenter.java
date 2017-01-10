package com.android.ocasa.settings;

import android.content.Context;

import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.util.ConnectionUtil;
import com.codika.androidmvprx.presenter.BaseRxPresenter;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 10/11/16.
 */

public class SettingsPresenter extends BaseRxPresenter<SettingsView> {

    private List<Receipt> uploadReceipts;

    public void checkLogout(){
        OcasaService.getInstance()
                .checkCloseReceipts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Receipt>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Receipt> receipts) {
                        if(receipts == null || receipts.isEmpty()){
                            getView().canLogout();
                            return;
                        }

                        uploadReceipts = receipts;

                        getView().needUpload();
                    }
                });
    }

    public void uploadReceipts(Context context) {

        if (!ConnectionUtil.isInternetAvailable(context)) {
            getView().notConnection();
            return;
        }

        OcasaService.getInstance()
                .uploadReceipts(uploadReceipts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getView().onUploadSuccess();
                    }
                });
    }

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
