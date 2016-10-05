package com.android.ocasa.receipt.base;

import android.content.Context;
import android.util.Log;

import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.presenter.BasePresenter;
import com.codika.androidmvprx.presenter.BaseRxPresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 07/07/16.
 */
public class BaseReceiptPresenter extends BaseRxPresenter<BaseReceiptView> {

    public BaseReceiptPresenter(){
    }

    public void header(long receiptId){

        OcasaService.getInstance()
                .receiptHeader(receiptId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReceiptFormViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Error", e.getMessage());
                    }

                    @Override
                    public void onNext(ReceiptFormViewModel header) {
                        getView().onHeaderSuccess(header);
                    }
                });
    }

    public void items(long receiptId){

        OcasaService.getInstance()
                .receiptItems(receiptId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TableViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Error", e.getMessage());
                    }

                    @Override
                    public void onNext(TableViewModel table) {
                        if(isViewAttached())
                            getView().onItemsSuccess(table);
                    }
                });
    }
}
