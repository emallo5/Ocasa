package com.android.ocasa.service;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.ocasa.api.OcasaApi;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.LayoutColumnDAO;
import com.android.ocasa.dao.LayoutDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.mail.Mail;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 07/07/16.
 */
public class OcasaService {

    static final String TAG = "OcasaService";

    private static OcasaService instance;

    private Context context;

    private OcasaApi api;

    public void init(Context context, OcasaApi api){
        this.context = context;
        this.api = api;
    }

    public static OcasaService getInstance() {
        if(instance == null)
            instance = new OcasaService();
        return instance;
    }

    public Observable<Layout> sync(final double latitude, final double longitude){

        new LayoutColumnDAO(context).deleteAll();

        return  applications()
                .flatMap(new Func1<List<Application>, Observable<Application>>() {
                    @Override
                    public Observable<Application> call(List<Application> applications) {
                        return Observable.from(applications);
                    }
                })
                .flatMap(new Func1<Application, Observable<Category>>() {
                    @Override
                    public Observable<Category> call(Application application) {
                        Log.v(TAG, "Start sync app " + application.getId() + ", " + application.getName());
                        return Observable.from(application.getCategories());
                    }
                })
                .flatMap(new Func1<Category, Observable<Layout>>() {
                             @Override
                             public Observable<Layout> call(Category category) {

                                 Log.v(TAG, "Start sync category " + category.getId() + ", " + category.getName());

                                 return Observable.from(category.getLayouts());
                             }
                         }
                )

//                        return Observable.zip(Observable.from(category.getActions()).flatMap(new Func1<Action, Observable<Table>>() {
//                            @Override
//                            public Observable<Table> call(Action action) {
//                                return Observable.just(action.getTable());
//                            }
//                        }).toList(), Observable.just(category.getLayouts()), new Func2<List<Table>, Collection<Layout>, List<Table>>() {
//                            @Override
//                            public List<Table> call(List<Table> tables, Collection<Layout> layouts) {
//
//                                for (Layout layout : layouts){
//                                    tables.add(layout.getTable());
//                                }
//
//                                return tables;
//                            }
//                        }).map(new Func1<List<Table>, List<Table>>() {
//                            @Override
//                            public List<Table> call(List<Table> tables) {
//
//                                Map<String, Table> distinctTables = new HashMap<>();
//
//                                for (int index = 0; index < tables.size(); index++){
//                                    Table table = tables.get(index);
//
//                                    distinctTables.put(table.getId(), table);
//                                }
//
//                                return new ArrayList<>(distinctTables.values());
//                            }
//                        }).last().flatMap(new Func1<List<Table>, Observable<Table>>() {
//                            @Override
//                            public Observable<Table> call(List<Table> tables) {
//                                return Observable.from(tables);
//                            }
//                        });
//                    }

                .flatMap(new Func1<Layout, Observable<Layout>>() {
                    @Override
                    public Observable<Layout> call(Layout layout) {
                        return tableWithCache(layout, latitude, longitude);
                    }
                }).flatMap(new Func1<Layout, Observable<Column>>() {
                    @Override
                    public Observable<Column> call(Layout layout) {

                        if(layout.getColumns() == null){
                            return Observable.empty();
                        }

                        List<Column> columns = new ArrayList<Column>();

                        for (LayoutColumn layoutColumn : layout.getColumns()){
                            columns.add(layoutColumn.getColumn());
                        }

                        return Observable.from(columns);
                    }
                }).filter(new Func1<Column, Boolean>() {
                    @Override
                    public Boolean call(Column column) {
                        return column.getFieldType() == FieldType.COMBO;
                    }
                }).flatMap(new Func1<Column, Observable<Layout>>() {
                    @Override
                    public Observable<Layout> call(Column column) {
                        Layout layout = column.getRelationship();

                        Log.v(TAG, "Start sync combo Layout " + layout.getId());

                        return tableWithCache(layout, latitude, longitude);
                    }
                }).last();
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

    public Observable<MenuViewModel> menu(){
        return Observable.create(new Observable.OnSubscribe<MenuViewModel>() {
            @Override
            public void call(Subscriber<? super MenuViewModel> subscriber) {
                subscriber.onNext(new MenuService().getMenu(context));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Layout> tableWithCache(Layout layout, final double latitude, double longitude){

        Log.v(TAG, "Start sync Layout " + layout.getId());

        String deviceId = SessionManager.getInstance().getDeviceId();

        return Observable.zip(
                api.columns(layout.getExternalID() + "|" + layout.getTable().getId(), deviceId, latitude, longitude)
                        .onErrorReturn(new Func1<Throwable, Layout>() {
                            @Override
                            public Layout call(Throwable throwable) {

                                Layout layout = new Layout();
                                layout.setColumns(new ArrayList<LayoutColumn>());

                                Table table = new Table();
                                table.setId("1");

                                layout.setTable(table);

                                return layout;
                            }
                        }),
                api.records(layout.getExternalID() + "|" + layout.getTable().getId(), deviceId, latitude, longitude)
                        .onErrorReturn(new Func1<Throwable, TableRecord>() {
                            @Override
                            public TableRecord call(Throwable throwable) {

                                TableRecord record = new TableRecord();
                                record.setRecords(new ArrayList<Record>());
                                return record;
                            }
                        }),
                new Func2<Layout, TableRecord, Layout>() {
                    @Override
                    public Layout call(Layout layout, TableRecord tableRecord) {
                        layout.getTable().setRecords(tableRecord.getRecords());
                        return layout;
                    }
                })
                .doOnNext(new Action1<Layout>() {
                    @Override
                    public void call(Layout layout) {
                        new TableService().saveTable(context, layout);
                    }
                });
    }

    public Observable<ReceiptFormViewModel> receiptHeader(long receiptId){
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
                RecordService service = new RecordService(context);

                return service.getFormFromRecord(recordId);
            }
        });
    }

    public Observable<Record> findRecord(long recordId){
        return Observable.just(recordId)
                .map(new Func1<Long, Record>() {
                    @Override
                    public Record call(Long recordId) {
                        RecordService service = new RecordService(context);

                        return service.findById(recordId);
                    }
                });
    }

    public Observable<FormViewModel> recordForm(String tableId){
        return Observable.just(tableId)
                .map(new Func1<String, FormViewModel>() {
                    @Override
                    public FormViewModel call(String tableId) {

                        RecordService service = new RecordService(context);
                        return service.getFormFromTable(tableId);
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
                        RecordService service = new RecordService(context);

                        return service.getFormFromRecordAndReceipt(recordId, receiptId);
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
                        RecordService service = new RecordService(context);

                        return service.getFromFromRecordAndReceipt(recordId, receiptId);
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

    public Observable<Boolean> saveReceipt(long receiptId, long[] recordIds, Location lastLocation, boolean close){

        return Observable.zip(Observable.just(receiptId),
                Observable.just(recordIds),
                Observable.just(lastLocation),
                Observable.just(close),
                new Func4<Long, long[], Location, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Long receiptId, long[] recordIds, Location lastLocation, Boolean close) {

                        Receipt receipt = new ReceiptService().updateReceiptItems(context, receiptId, recordIds, lastLocation, close);

                        if(close){
                            sendMail(receipt);
                        }

                        return true;
                    }
                });
    }

    private void sendMail(Receipt receipt){

        Mail m = new Mail("oviedoignacio91@gmail.com", "Igna1991.");

        String[] toArr = {"oviedoignacio91@gmail.com"};//, "juan.sappracone@ocasa.com"};
        m.setTo(toArr);
        m.setFrom("example@example.com");
        m.setSubject(receipt.getAction().getName());

        StringBuilder body = new StringBuilder();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Action.class, new Menu.ActionDeserializer())
                .registerTypeAdapter(Record.class, new TableRecord.RecordDeserializer())
                .registerTypeAdapter(Record.class, new TableRecord.RecordSerializer())
                .registerTypeAdapter(Column.class, new HttpTable.ColumnDeserializer())
                .create();

        TableRecord record = new TableRecord();

        List<Record> records = new ArrayList<>();

        for (ReceiptItem item : receipt.getItems()){

            List<Field> headerFields = new ArrayList<>();

            Record receiptRecord = new Record();
            receiptRecord.setExternalId(item.getRecord().getExternalId());

            List<ColumnAction> headers = new ColumnActionDAO(context)
                    .findColumnsForActionAndType(receipt.getAction().getId(), ColumnAction.ColumnActionType.HEADER);

            for (ColumnAction columnAction : headers){
                headerFields.add(item.getRecord().getFieldForColumn(columnAction.getColumn().getId()));
            }

            receiptRecord.setFields(headerFields);
            records.add(receiptRecord);
        }

        record.setRecords(records);

        api.upload(record, receipt.getAction().getId(), SessionManager.getInstance().getDeviceId(), 0, 0)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Receipt>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Receipt receipt) {
                Log.v(TAG, "Upload Receipt success " + receipt.getNumber());
            }
        });

        m.setBody(gson.toJson(record));

        try {
            if(m.send()) {
                Log.v("MailApp", "Email sent");
            } else {
                Log.v("MailApp", "Could not send email");
            }
        } catch(Exception e) {
            Log.e("MailApp", "Could not send email", e);
        }
    }

    public Observable<Menu> login(LoginCredentials credentials) {
        return api.login(credentials, credentials.getImei())
                .doOnNext(new Action1<Menu>() {
                    @Override
                    public void call(Menu menu) {
                        new MenuService().save(context, menu);
                    }
                });
    }

    public List<Column> getDetailColumnsForReceipt(long receiptId){

        List<Column> columns = new ArrayList<>();

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);

        List<ColumnAction> detailColumns =  new ColumnActionDAO(context)
                .findColumnsForActionAndType(receipt.getAction().getId(), ColumnAction.ColumnActionType.DETAIL);

        for (ColumnAction columnAction : detailColumns){
            columns.add(columnAction.getColumn());
        }

        return columns;
    }

    public void updateRecord(Record record){
        new RecordService(context).saveRecord(record);
    }
}
