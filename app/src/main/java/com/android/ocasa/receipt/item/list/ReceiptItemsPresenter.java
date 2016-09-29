package com.android.ocasa.receipt.item.list;

import android.util.Log;

import com.android.ocasa.core.TablePresenter;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.TableViewModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 03/08/16.
 */
public class ReceiptItemsPresenter extends TablePresenter {

    static final String TAG = "ReceiptItemsPresenter";

    public void load(long receiptId){
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

                    }

                    @Override
                    public void onNext(TableViewModel tableViewModel) {

                        if(getTable() != null)
                            return;

                        Log.v(TAG, "Receipt items loaded");

                        getView().onTableLoadSuccess(tableViewModel);
                        setTable(tableViewModel);
                    }
                });
    }
}
