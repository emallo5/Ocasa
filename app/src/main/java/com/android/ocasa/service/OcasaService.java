package com.android.ocasa.service;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

import com.android.ocasa.api.ApiManager;
import com.android.ocasa.cache.CacheManager;
import com.android.ocasa.cache.dao.ColumnActionDAO;
import com.android.ocasa.cache.dao.NewRecordReadDAO;
import com.android.ocasa.cache.dao.ReceiptItemDAO;
import com.android.ocasa.httpmodel.Archive;
import com.android.ocasa.httpmodel.ControlBody;
import com.android.ocasa.httpmodel.ControlResponse;
import com.android.ocasa.httpmodel.GenericResponse;
import com.android.ocasa.httpmodel.LogOutBody;
import com.android.ocasa.httpmodel.MediaBody;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.RecordArchive;
import com.android.ocasa.httpmodel.ResponseImage;
import com.android.ocasa.httpmodel.ResponseReceipt;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.LocationModel;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.MassiveColumn;
import com.android.ocasa.model.NewRecordRead;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.RecordMassive;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.sync.SyncIntentSerivce;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.FileHelper;
import com.android.ocasa.util.MediaUtils;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * Created by ignacio on 07/07/16.
 */
public class OcasaService {

    static final String TAG = "OcasaService";
    static final String SERVICE_RESPONSE = "SERVICE RESPONSE: ";

    static OcasaService instance;

    private Context context;

    private ApiManager apiManager;
    private CacheManager cacheManager;

    public void init(Context context, ApiManager apiManager, CacheManager cacheManager){
        this.context = context;
        this.apiManager = apiManager;
        this.cacheManager = cacheManager;
    }

    public static OcasaService getInstance() {
        if(instance == null)
            instance = new OcasaService();
        return instance;
    }

    public Observable<Layout> sync(final double latitude, final double longitude){

        cacheManager.cleanLayoutColumn();

        Log.d(TAG, "Init SYNC");

        return  categories()
                .flatMap(new Func1<Category, Observable<Layout>>() {
                    @Override
                    public Observable<Layout> call(Category category) {

                        Log.v(TAG, "Start sync category " + category.getId() + ", " + category.getName());

                        return Observable.from(category.getLayouts());
                    }
                }).distinct(new Func1<Layout, String>() {
                    @Override
                    public String call(Layout layout) {
                        return layout.getExternalID();
                    }
                })
                .flatMap(new Func1<Layout, Observable<Layout>>() {
                    @Override
                    public Observable<Layout> call(Layout layout) {

                        return tableWithCache(layout, latitude, longitude);
                    }
                }).flatMap(new Func1<Layout, Observable<Column>>() {
                    @Override
                    public Observable<Column> call(Layout layout) {

//                        if(layout.getColumns() == null || layout.getColumns().isEmpty()){
//                            return Observable.<Column>empty().defaultIfEmpty(new Column());
//                        }
                        List<Column> columns = new ArrayList<>();

                        for (LayoutColumn layoutColumn : layout.getColumns()){
                            columns.add(layoutColumn.getColumn());
                        }

                        return Observable.from(columns);
                    }
                }).filter(new Func1<Column, Boolean>() {
                    @Override
                    public Boolean call(Column column) {
                        return column.isCombo();
                    }
                }).flatMap(new Func1<Column, Observable<Layout>>() {
                    @Override
                    public Observable<Layout> call(Column column) {
                        Layout layout = column.getRelationship();

                        Log.v(TAG, "Start sync combo Layout " + layout.getExternalID() + " Table " + layout.getTable().getId());

                        return tableWithCache(layout, latitude, longitude);
                    }
                }).distinct(new Func1<Layout, String>() {
                    @Override
                    public String call(Layout layout) {
                        return layout.getExternalID();
                    }
                }).last();
    }

    private Observable<Category> categories(){
        return cacheManager.applications()
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
                });
    }

    public Observable<MenuViewModel> menu(){
        return cacheManager.menu();
    }

    private Observable<Layout> tableWithCache(Layout layout, double latitude, double longitude){

        Log.v(TAG, "Start sync Layout " + layout.getExternalID() + " Table " + layout.getTable().getId());

        String deviceId = SessionManager.getInstance().getDeviceId();

        return apiManager.layout(layout, latitude, longitude, deviceId)
                .doOnNext(new Action1<Layout>() {
                    @Override
                    public void call(Layout layout) {
                        cacheManager.saveLayout(layout);
                    }
                });
    }

    public Observable<ReceiptFormViewModel> receiptHeader(long receiptId){
        return cacheManager.receiprHeader(receiptId);
    }

    public Observable<TableViewModel> receiptItems(long receiptId){
        return cacheManager.receiptItems(receiptId);
    }

    public Observable<List<CellViewModel>> findReceiptItems(long receiptId, long[] recordIds){
        return cacheManager.findReceiptItems(receiptId, recordIds);
    }

    public Observable<ReceiptTableViewModel> receiptsByAction(String actionId){
        return cacheManager.receiptsByAction(actionId);
    }

    public Observable<FormViewModel> record(long recordId){
        return cacheManager.record(recordId);
    }

    public Observable<Record> findRecord(long recordId){
        return cacheManager.findRecord(recordId);
    }

    public Observable<FormViewModel> recordForm(String tableId){
        return cacheManager.recordForm(tableId);
    }

    public Observable<FormViewModel> headerReceipt(String actionId){
        return cacheManager.headerReceipt(actionId);
    }

    public Observable<FormViewModel> receiptItem(long recordId, long receiptId){
        return cacheManager.receiptItem(recordId, receiptId);
    }

    public Observable<FormViewModel> updateReceiptItem(long recordId, long receiptId){
        return cacheManager.updateReceiptItem(recordId, receiptId);
    }

    public Observable<FormViewModel> receiptStatus(long receiptId){
        return cacheManager.receiptStatus(receiptId);
    }

    public Observable<TableViewModel> table(final String layoutId, String query, long[] excludeIds){
        return cacheManager.table(layoutId, query, excludeIds);
    }

    public Observable<TableViewModel> receiptAvailableItems(long receiptId, String query, long[] excludeIds){
        return cacheManager.receiptAvailableItems(receiptId, query, excludeIds);
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
                            upload(receipt);
                        }

                        return true;
                    }
                });
    }

    private void upload(final Receipt receipt) {


        final TableRecord record = new TableRecord();

        RecordMassive recordMassive = new RecordMassive();

        List<Record> records = new ArrayList<>();

        List<Field> mediaFiles = new ArrayList<>();

        for (ReceiptItem item : receipt.getItems()){

            List<Field> headerFields = new ArrayList<>();

            Record receiptRecord = new Record();

            if (item.getRecord().getExternalId() == null) return;

            receiptRecord.setExternalId(item.getRecord().getExternalId());

            List<ColumnAction> headers = new ColumnActionDAO(context)
                    .findColumnsForActionAndType(receipt.getAction().getId(), ColumnAction.ColumnActionType.HEADER);

            for (ColumnAction columnAction : headers){
                headerFields.add(item.getRecord().getFieldForColumn(columnAction.getColumn().getId()));
            }

            List<ColumnAction> details = new ColumnActionDAO(context)
                    .findColumnsForActionAndType(receipt.getAction().getId(), ColumnAction.ColumnActionType.DETAIL);

            for (ColumnAction columnAction : details){
                if(columnAction.getColumn().getFieldType() != FieldType.PHOTO &&
                        columnAction.getColumn().getFieldType() != FieldType.SIGNATURE)
                    headerFields.add(item.getRecord().getFieldForColumn(columnAction.getColumn().getId()));
                else
                    mediaFiles.add(item.getRecord().getFieldForColumn(columnAction.getColumn().getId()));
            }

            List<NewRecordRead> codes = new NewRecordReadDAO(context).findByRecordId(item.getRecord().getId());
            if (codes.size() > 0) {
                recordMassive.setId(item.getRecord().getExternalId());
                for (NewRecordRead rr : codes) recordMassive.addCode(rr.getRead());
                for (Field field : headerFields) recordMassive.addColumn(new MassiveColumn(field.getColumn().getId(), field.getValue()));
            }

            receiptRecord.setFields(headerFields);
            records.add(receiptRecord);
        }

        record.setRecords(records);

        ArrayList<MediaBody> bodys = new ArrayList<>();

        for (Field media : mediaFiles) {

            MediaBody body = new MediaBody();

            if(media != null && media.getValue() != null && !media.getValue().isEmpty()) {

                File file = new File(Environment.getExternalStorageDirectory(), "/Ocasa/" + media.getValue());

                RecordArchive recordArchive = new RecordArchive();
                recordArchive.setId(media.getRecord().getExternalId());

                Archive archive = new Archive();
                archive.setType("image/jpg");
                archive.setBase("base64");

                if (!file.exists()) break;

                archive.setData(MediaUtils.convertMediaToBase64(file.getPath()));

                recordArchive.addArchive(archive);

                body.addRecordArchive(recordArchive);
                bodys.add(body);
            }
        }

        Collection<ReceiptItem> list = receipt.getItems();
        final String id = list.iterator().next().getRecord().getExternalId();
        try {
            FileHelper.getInstance().writeToFile("record " + id.substring(27, 31));
        } catch (Exception e) {}

        if (bodys.size() != 0)
            uploadImages(receipt.getAction().getTable().getId(), bodys, record, receipt, id, recordMassive);
        else {
            if (recordMassive.isNotEmpty()) uploadMassiveRecord(receipt, recordMassive);
            else uploadInfo(record, receipt, id);
        }
    }

    private void uploadImages(final String tableId, final ArrayList<MediaBody> bodys, final TableRecord record, final Receipt receipt, final String id, final RecordMassive recordMassive) {
        MediaBody body = bodys.get(0);

        apiManager.uploadImage(tableId, body, SessionManager.getInstance().getDeviceId())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseImage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        try {
                            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                        } catch (Throwable pluginException) {}

                        try {
                            FileHelper.getInstance().writeToFile("imgErr " + id.substring(27, 31) + " by: " + e.getMessage());
                        } catch (Exception f) {}
                    }

                    @Override
                    public void onNext(ResponseImage s) {
                        try {
                            FileHelper.getInstance().writeToFile("imgSuc " + id.substring(27, 31) + " server " + s.getDescription().substring(27, 31));
                        } catch (Exception e) {}

                        if (s.getStatus() != 0) return;

                        bodys.remove(0);

                        if (bodys.size() != 0)
                            uploadImages(tableId, bodys, record, receipt, id, recordMassive);
                        else {
                            if (recordMassive.isNotEmpty())
                                uploadMassiveRecord(receipt, recordMassive);
                            else uploadInfo(record, receipt, id);
                        }
                    }
                });
    }

    private void uploadMassiveRecord(final Receipt receipt, RecordMassive recordMassive) {

        apiManager.uploadMassive(recordMassive, receipt.getAction().getId() + "|" + receipt.getAction().getTable().getId(),
                SessionManager.getInstance().getDeviceId()).retry(3)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseReceipt>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(ResponseReceipt responseReceipt) {
                                   updateReceiptClosed(receipt.getId());
                               }
                           });
    }

    private void uploadInfo(TableRecord record, final Receipt receipt, final String id) {
        apiManager.upload(record, receipt.getAction().getId() + "|" + receipt.getAction().getTable().getId(),
                SessionManager.getInstance().getDeviceId(), 0, 0).retry(3)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseReceipt>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        FileHelper.getInstance().writeToFile("infErr " + id.substring(27, 31) + " by: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseReceipt rec) {
                        if (rec.getId() != null) {
                            try {
                                FileHelper.getInstance().writeToFile("infSuc " + id.substring(27, 31) + " server " + rec.getId().substring(27, 31));
                            } catch (Exception e) {}
                        }
                        updateReceiptClosed(receipt.getId());
                    }
                });
    }

    public Observable<Menu> login(LoginCredentials credentials) {
        return apiManager.login(credentials)
                .doOnNext(new Action1<Menu>() {
                    @Override
                    public void call(Menu menu) {
                        cacheManager.saveMenu(menu);
                    }
                });
    }

    public Observable<GenericResponse> logout(LogOutBody body) {
        return apiManager.logout(body);
    }

    public Observable<Void> cleanDB() {
        return cacheManager.cleanDb(context);
    }

    public Observable<ControlResponse> controlSync (ControlBody body) {
        return apiManager.controlSync(body);
    }

    public FormViewModel getDetailFormForReceipt(long recordId, long receiptId){
        return cacheManager.getDetailColumnsForReceipt(recordId, receiptId);
    }

    public void updateRecord(Record record){
        cacheManager.updateRecord(record);
    }

    public Observable<Void> logout() {
        return cacheManager.cleanDb(context);
    }

    public Observable<Void> closeReceipt(final long receiptId) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {

                ReceiptService service = new ReceiptService();

                Receipt receipt = service.findReceiptById(context, receiptId);
                receipt.setClose();

//                service.updateReceipt(context, receipt);

                receipt.setItems(new ReceiptItemDAO(context).findForReceipt(receipt.getId()));

                upload(receipt);

                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }

    public void updateReceiptClosed (long receiptId) {
        ReceiptService service = new ReceiptService();
        Receipt receipt = service.findReceiptById(context, receiptId);
        receipt.setClose();
        service.updateReceipt(context, receipt);
    }

    public Observable<List<Receipt>> checkCloseReceipts() {
        return Observable.create(new Observable.OnSubscribe<List<Receipt>>() {
            @Override
            public void call(Subscriber<? super List<Receipt>> subscriber) {

                List<Receipt> opens = new ReceiptService().getOpenReceipts(context);

                subscriber.onNext(opens);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> uploadReceipts(final List<Receipt> uploadReceipts) {

        int lap = ConfigHelper.getInstance().getAppConfiguration().getPodDelay();

        return Observable.zip(Observable.from(uploadReceipts), Observable.interval(lap, TimeUnit.SECONDS), new Func2<Receipt, Long, Boolean>() {
            @Override
            public Boolean call(Receipt receipt, Long l) {
                upload(receipt);
                Log.d(SyncIntentSerivce.TAG, "Sync record: " + receipt.getId());
                return true;
            }
        });
    }

    public Observable<Void> sendLocationData(LocationModel data) {
        return apiManager.sendLocation(data);
    }
}
