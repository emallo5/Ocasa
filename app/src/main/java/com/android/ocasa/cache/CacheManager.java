package com.android.ocasa.cache;

import android.content.Context;

import com.android.ocasa.cache.dao.ActionDAO;
import com.android.ocasa.cache.dao.ApplicationDAO;
import com.android.ocasa.cache.dao.CategoryDAO;
import com.android.ocasa.cache.dao.ColumnActionDAO;
import com.android.ocasa.cache.dao.ColumnDAO;
import com.android.ocasa.cache.dao.FieldDAO;
import com.android.ocasa.cache.dao.HistoryDAO;
import com.android.ocasa.cache.dao.LayoutColumnDAO;
import com.android.ocasa.cache.dao.LayoutDAO;
import com.android.ocasa.cache.dao.ReceiptDAO;
import com.android.ocasa.cache.dao.ReceiptItemDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.cache.dao.StatusDAO;
import com.android.ocasa.cache.dao.TableDAO;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.FieldService;
import com.android.ocasa.service.MenuService;
import com.android.ocasa.service.ReceiptService;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.service.TableService;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by ignacio on 13/10/16.
 */

public class CacheManager {

    private Context context;

    public CacheManager(Context context){
        this.context = context;
    }

    public void cleanLayoutColumn(){
        new LayoutColumnDAO(context).deleteAll();
    }

    public void saveMenu(Menu menu){
        MenuService menuService = new MenuService();
        menuService.save(context, menu);
    }

    public Observable<MenuViewModel> menu(){

        return Observable.create(new Observable.OnSubscribe<MenuViewModel>() {
            @Override
            public void call(Subscriber<? super MenuViewModel> subscriber) {

                MenuService menuService = new MenuService();

                subscriber.onNext(menuService.getMenu(context));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Application>> applications(){
        return Observable.create(new Observable.OnSubscribe<List<Application>>() {
            @Override
            public void call(Subscriber<? super List<Application>> subscriber) {
                List<Application> apps = new ApplicationDAO(context).findAll();
                subscriber.onNext(apps);
                subscriber.onCompleted();
            }
        });
    }

    public void saveLayout(Layout layout){
        TableService tableService = new TableService();
        tableService.saveTable(context, layout);
    }

    public Observable<ReceiptFormViewModel> receiprHeader(long receiptId){
        return Observable.just(receiptId).map(new Func1<Long, ReceiptFormViewModel>() {
            @Override
            public ReceiptFormViewModel call(Long receiptId) {
                return new ReceiptService().getHeaderByReceipt(context, receiptId);
            }
        });
    }

    public Observable<TableViewModel> receiptItems(long receiptId){
        return Observable.just(receiptId).map(new Func1<Long, TableViewModel>() {
            @Override
            public TableViewModel call(Long receiptId) {
                return new ReceiptService().getItemsByReceipt(context, receiptId);
            }
        });
    }

    public Observable<List<CellViewModel>> findReceiptItems(long receiptId, long[] recordIds){
        return Observable.zip(Observable.just(receiptId), Observable.just(recordIds), new Func2<Long, long[], List<CellViewModel>>() {
            @Override
            public List<CellViewModel> call(Long receiptId, long[] recordIds) {
                return new ReceiptService().findReceiptItems(context, receiptId, recordIds);
            }
        });
    }

    public Observable<ReceiptTableViewModel> receiptsByAction(String actionId){
        return Observable.just(actionId).map(new Func1<String, ReceiptTableViewModel>() {
            @Override
            public ReceiptTableViewModel call(String actionId) {
                return new ReceiptService().getReceiptForAction(context, actionId);
            }
        });
    }

    public Observable<FormViewModel> record(long recordId){
        return Observable.just(recordId).map(new Func1<Long, FormViewModel>() {
            @Override
            public FormViewModel call(Long recordId) {
                RecordService service = new RecordService();

                return service.getFormFromRecord(context, recordId);
            }
        });
    }

    public Observable<Record> findRecord(long recordId){
        return Observable.just(recordId)
                .map(new Func1<Long, Record>() {
                    @Override
                    public Record call(Long recordId) {
                        RecordService service = new RecordService();

                        return service.findRecord(context, recordId);
                    }
                });
    }

    public Observable<FormViewModel> recordForm(String tableId){
        return Observable.just(tableId)
                .map(new Func1<String, FormViewModel>() {
                    @Override
                    public FormViewModel call(String tableId) {

                        RecordService service = new RecordService();
                        return service.getFormFromTable(context, tableId);
                    }
                });
    }

    public Observable<FormViewModel> headerReceipt(String actionId){
        return Observable.just(actionId).map(new Func1<String, FormViewModel>() {
            @Override
            public FormViewModel call(String actionId) {
                return new ReceiptService().newReceiptHeader(context, actionId);
            }
        });
    }

    public Observable<FormViewModel> receiptItem(long recordId, long receiptId){
        return Observable.zip(
                Observable.just(recordId),
                Observable.just(receiptId),
                new Func2<Long, Long, FormViewModel>() {
                    @Override
                    public FormViewModel call(Long recordId, Long receiptId) {
                        RecordService service = new RecordService();

                        return service.getFormFromRecordAndReceipt(context, recordId, receiptId);
                    }
                });
    }

    public Observable<FormViewModel> updateReceiptItem(long recordId, long receiptId){
        return Observable.zip(
                Observable.just(recordId),
                Observable.just(receiptId),
                new Func2<Long, Long, FormViewModel>() {
                    @Override
                    public FormViewModel call(Long recordId, Long receiptId) {
                        RecordService service = new RecordService();

                        return service.getFromFromRecordAndReceipt(context, recordId, receiptId);
                    }
                });
    }

    public Observable<FormViewModel> receiptStatus(long receiptId){
        return Observable.just(receiptId).map(new Func1<Long, FormViewModel>() {
            @Override
            public FormViewModel call(Long receiptId) {
                return new ReceiptService().getStatus(context, receiptId);
            }
        });
    }

    public Observable<TableViewModel> table(final String layoutId, String query, long[] excludeIds){

        return Observable.zip(Observable.just(layoutId),
                Observable.just(query),
                Observable.just(excludeIds), new Func3<String, String, long[], TableViewModel>() {
                    @Override
                    public TableViewModel call(String tableId, String query, long[] excludeIds) {
                        return new TableService().getRecords(context, layoutId, query, excludeIds);
                    }
                });
    }

    public Observable<TableViewModel> receiptAvailableItems(long receiptId, String query, long[] excludeIds){
        return Observable.zip(
                Observable.just(receiptId),
                Observable.just(query),
                Observable.just(excludeIds), new Func3<Long, String, long[], TableViewModel>() {
                    @Override
                    public TableViewModel call(Long receiptId, String query, long[] excludeIds) {
                        return new ReceiptService().getAvailableItems(context, receiptId, query);
                    }
                });
    }

    public FormViewModel getDetailColumnsForReceipt(long receiptId){

        FormViewModel form = new FormViewModel();
        form.setColor("#33BDC2");

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        List<ColumnAction> detailColumns =  new ColumnActionDAO(context)
                .findColumnsForActionAndType(receipt.getAction().getId(), ColumnAction.ColumnActionType.DETAIL);

        for (ColumnAction columnAction : detailColumns){

            Column column = columnAction.getColumn();

            FieldViewModel fieldViewModel = new FieldService().getFieldFromColumn(context, column);
            fieldViewModel.setEditable(true);

            form.addField(fieldViewModel);
        }

        return form;
    }

    public void updateRecord(Record record){
        new RecordService().saveRecord(context, record);
    }

    public Observable<Void> cleanDb(final Context context) {

        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {

                new ApplicationDAO(context).deleteAll();
                new CategoryDAO(context).deleteAll();
                new TableDAO(context).deleteAll();
                new ReceiptDAO(context).deleteAll();
                new RecordDAO(context).deleteAll();
                new FieldDAO(context).deleteAll();
                new LayoutColumnDAO(context).deleteAll();
                new ColumnDAO(context).deleteAll();
                new ColumnActionDAO(context).deleteAll();
                new LayoutDAO(context).deleteAll();
                new HistoryDAO(context).deleteAll();
                new ReceiptItemDAO(context).deleteAll();
                new StatusDAO(context).deleteAll();
                new ActionDAO(context).deleteAll();

                SessionManager.getInstance().cleanSession();

                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }
}
