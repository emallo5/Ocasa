package com.android.ocasa.receipt.item.available;

import com.android.ocasa.core.TablePresenter;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.TableViewModel;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 21/07/16.
 */
public class AvailableItemsPresenter extends TablePresenter {

    public void load(long receiptId) {
        OcasaService.getInstance()
                .receiptAvailableItems(receiptId, null, null)
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
                    public void onNext(TableViewModel table) {
                        getView().onTableLoadSuccess(table);
                        setTable(table);
                    }
                });
    }
}
