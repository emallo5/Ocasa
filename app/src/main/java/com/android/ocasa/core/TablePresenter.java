package com.android.ocasa.core;

import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.presenter.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 18/07/16.
 */
public class TablePresenter extends BasePresenter<TableView> {

    private TableViewModel table;

    public void load(String layoutId){
        if(layoutId != null)
            load(layoutId, null);
    }

    public void load(String layoutId, String query){

        OcasaService.getInstance()
                .table(layoutId, query, null)
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
                        setTable(table);
                    }
                });
    }

    public void setTable(TableViewModel table){
        this.table = table;
    }

    public TableViewModel getTable(){
        return table;
    }

    public CellViewModel getItemAtPosition(int position){
        return table.getCells().get(position);
    }
}
