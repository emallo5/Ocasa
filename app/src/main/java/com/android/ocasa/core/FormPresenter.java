package com.android.ocasa.core;

import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.FormViewModel;
import com.codika.androidmvp.presenter.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 11/07/16.
 */
public class FormPresenter extends BasePresenter<FormView> {

    public void load(long recordId){
        OcasaService.getInstance()
                .record(recordId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FormViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FormViewModel formViewModel) {
                        getView().onFormSuccess(formViewModel);
                    }
                });
    }
}
