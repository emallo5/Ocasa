package com.android.ocasa.receipt.header;

import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.FormViewModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 11/07/16.
 */
public class EditHeaderReceiptPresenter extends FormPresenter {

    public void load(String actionId) {
        OcasaService.getInstance()
                .headerReceipt(actionId)
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
