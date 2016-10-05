package com.android.ocasa.home.menu;

import com.android.ocasa.model.Application;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.codika.androidmvp.presenter.BasePresenter;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 18/07/16.
 */
public class MenuPresenter extends BasePresenter<MenuView> {

    public void menu(){
        OcasaService.getInstance().menu()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<MenuViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MenuViewModel menu) {
                        getView().onMenuLoadSuccess(menu);
                    }
                });
    }
}
