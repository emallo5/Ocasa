package com.android.ocasa.receipt.status;

import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.FormViewModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 18/07/16.
 */
public class ReceiptStatusPresenter extends FormPresenter {

    public void load(long receiptId){
        OcasaService.getInstance()
                .receiptStatus(receiptId)
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
                    public void onNext(FormViewModel form) {
                        getView().onFormSuccess(form);
                    }
                });
    }
}
