package com.android.ocasa.receipt.list;

import android.util.Log;
import android.widget.Toast;

import com.android.ocasa.httpmodel.ControlBody;
import com.android.ocasa.httpmodel.ControlResponse;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.service.ReceiptService;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.viewmodel.FormViewModel;
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ReceiptTableViewModel receiptTableViewModel) {
                        if (getView() != null)
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
                        getView().onCloseReceiptSuccess();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        getView().onCloseReceiptSuccess();
                    }
                });
    }

    public void control() {
        ControlBody body = new ControlBody();
        body.setImei(SessionManager.getInstance().getDeviceId());

        OcasaService.getInstance().controlSync(body).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ControlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ControlResponse controlResponse) {
                        if (getView() != null)
                            getView().onControlSynResponse(controlResponse);
                    }
                });
    }

    public void sync() {

        OcasaService.getInstance()
                .sync(0, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Layout>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onSyncError();
                    }

                    @Override
                    public void onNext(Layout layout) {
                        getView().onSyncSuccess();
                    }
                });
    }

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
