package com.android.ocasa.core;

import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.presenter.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 18/07/16.
 */
public class TablePresenter extends BasePresenter<TableView> {

    public void load(String tableId){
        load(tableId, null);
    }

    public void load(String tableId, String query){
        OcasaService.getInstance()
                .table(tableId, query, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                    }
                });
    }
}
