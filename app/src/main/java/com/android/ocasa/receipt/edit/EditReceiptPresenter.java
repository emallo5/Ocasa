package com.android.ocasa.receipt.edit;

import android.util.Log;

import com.android.ocasa.receipt.base.BaseReceiptPresenter;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 07/07/16.
 */
public class EditReceiptPresenter extends BaseReceiptPresenter{


    public EditReceiptPresenter(){
    }

    public void findItem(long receiptId, long recordId){
        findItems(receiptId, new long[]{recordId});
    }

    public void findItems(long receiptId, long[] recordIds){
        OcasaService.getInstance()
                .findReceiptItems(receiptId, recordIds)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<CellViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Error", e.getMessage());
                    }

                    @Override
                    public void onNext(List<CellViewModel> items) {
                        ((EditReceiptView)getView()).onItemsFoundSuccess(items);
                    }
                });
    }

}
