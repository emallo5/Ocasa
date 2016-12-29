package com.android.ocasa.receipt.list;

import android.util.Log;
import android.widget.Toast;

import com.android.ocasa.model.Receipt;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.service.ReceiptService;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.codika.androidmvp.presenter.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptListPresenter extends BasePresenter<ReceiptListView> {

    public void receipts(String actionId){
        OcasaService.getInstance()
                .receiptsByAction(actionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ReceiptTableViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Error", e.getMessage());
                    }

                    @Override
                    public void onNext(ReceiptTableViewModel receiptTableViewModel) {
                        getView().onReceiptsLoadSuccess(receiptTableViewModel);
                    }
                });
    }

    public void close(final long receiptId) {

        OcasaService.getInstance()
                .closeReceipt(receiptId)
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
                        getView().onCloseReceiptSuccess();
                    }
                });
    }
}
