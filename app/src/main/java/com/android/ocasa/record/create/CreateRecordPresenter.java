package com.android.ocasa.record.create;

import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.FormViewModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 14/07/16.
 */
public class CreateRecordPresenter extends FormPresenter {

    public void load(String table){
        OcasaService.getInstance()
                .recordForm(table)
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
