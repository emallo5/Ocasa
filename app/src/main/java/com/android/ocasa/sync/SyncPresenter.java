package com.android.ocasa.sync;

import android.util.Log;

import com.android.ocasa.model.ApiError;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.session.SessionPresenter;
import com.android.ocasa.session.SessionSubscriber;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.Constants;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;

/**
 * Created by ignacio on 21/06/16.
 */
public class SyncPresenter extends SessionPresenter<SyncView> {

    static final String TAG = "SyncPresenter";

    private Subscription subscription;

    private AsyncSubject<Layout> subject;

    public SyncPresenter() {
        subject = AsyncSubject.create();
    }

    public void sync() {

        subscription = OcasaService.getInstance()
                .sync(0, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    @Override
    public void onAttachView(SyncView view) {
        super.onAttachView(view);
        addSubscription(subject.asObservable()
                .subscribe(new SyncSubscriber(this)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null)
            subscription.unsubscribe();
    }

    private class SyncSubscriber extends SessionSubscriber<Layout>{

        protected SyncSubscriber(SyncPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Log.e(TAG, "Sync error: " + e.getMessage());
            getView().onSyncError();
        }

        @Override
        public void onHttpError(ApiError error) {
        }

        @Override
        public void onNext(Layout menu) {
            Log.v(TAG, "Sync completed");
            getView().onSyncFinish();
        }
    }
}
