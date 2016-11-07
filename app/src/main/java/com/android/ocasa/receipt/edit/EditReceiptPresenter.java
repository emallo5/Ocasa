package com.android.ocasa.receipt.edit;

import android.location.Location;
import android.util.Log;

import com.android.ocasa.cache.dao.ReceiptDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.receipt.base.BaseReceiptPresenter;
import com.android.ocasa.receipt.base.BaseReceiptView;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.CellViewModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;

/**
 * Created by ignacio on 07/07/16.
 */
public class EditReceiptPresenter extends BaseReceiptPresenter{

    static final String TAG = "EditReceiptPresenter";

    private Subscription subscription;

    private AsyncSubject<Boolean> subject;

    private List<Column> detailColumns;
    private int currentColumn = 0;

    private long currentRecordId;

    public EditReceiptPresenter(){
        subject = AsyncSubject.create();
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
                        Log.v(TAG, "Receipt items found " + items.size());

                        ((EditReceiptView)getView()).onItemsFoundSuccess(items);
                    }
                });
    }

    public void saveReceipt(long id, long[] recordsIds, Location lastLocation, boolean close){

        if(recordsIds == null || recordsIds.length == 0){
            ((EditReceiptView)getView()).onReceiptItemsEmpty();
            return;
        }

        ((EditReceiptView)getView()).showProgress();

        subscription = OcasaService.getInstance()
                .saveReceipt(id, recordsIds, lastLocation, close)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    public void checkDetailFields(long receiptId, long recordId){

        this.currentRecordId = recordId;

        if(detailColumns == null)
            detailColumns = OcasaService.getInstance().getDetailColumnsForReceipt(receiptId);

        if(detailColumns.size() >  0){
            loadDetail();
        }

    }

    public void updateValue(final String value){

        OcasaService.getInstance().findRecord(currentRecordId).subscribe(new Subscriber<Record>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Record record) {
                Column column = detailColumns.get(currentColumn);

                Field field = record.getFieldForColumn(column.getId());
                field.setValue(value);

                OcasaService.getInstance().updateRecord(record);
            }
        });

    }

    public void next(){
        currentColumn += 1;
        loadDetail();
    }

    private void loadDetail(){

        if(currentColumn >=  detailColumns.size()){
            currentColumn = 0;
            return;
        }

        Column column = detailColumns.get(currentColumn);

        switch (column.getFieldType()){
            case SIGNATURE:
                ((EditReceiptView)getView()).onTakeSignature(column.getName());
                break;
            case PHOTO:
                ((EditReceiptView)getView()).onTakePhoto(column.getName());
                break;
            /*case TEXT:
                ((EditReceiptView)getView()).onTakeText(column.getName());
                break;
            case COMBO:
                ((EditReceiptView)getView()).onTakeText(column.getName());
                break;*/
            default:
                next();
        }

    }


    @Override
    public void onAttachView(BaseReceiptView view) {
        super.onAttachView(view);
        addSubscription(subject.asObservable()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Error", e.getMessage());
                        ((EditReceiptView)getView()).hideProgress();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        ((EditReceiptView)getView()).hideProgress();
                        ((EditReceiptView)getView()).onReceiptSaveSuccess();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription != null)
            subscription.unsubscribe();
    }
}
