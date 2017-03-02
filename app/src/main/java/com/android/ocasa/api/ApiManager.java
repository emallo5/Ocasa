package com.android.ocasa.api;

import com.android.ocasa.httpmodel.ControlBody;
import com.android.ocasa.httpmodel.ControlResponse;
import com.android.ocasa.httpmodel.GenericResponse;
import com.android.ocasa.httpmodel.LogOutBody;
import com.android.ocasa.httpmodel.MediaBody;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.ResponseImage;
import com.android.ocasa.httpmodel.ResponseReceipt;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.LocationModel;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.SyncUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by ignacio on 13/10/16.
 */

public class ApiManager {

    private final OcasaApi api;

    public ApiManager(OcasaApi api) {
        this.api = api;
    }

    public Observable<Menu> login(LoginCredentials credentials){
        return api.login(credentials, credentials.getImei());
    }

    public Observable<GenericResponse> logout (LogOutBody body) {
        return api.logout(body);
    }

    public Observable<ControlResponse> controlSync (ControlBody body) {
        return api.controlSync(body);
    }

    public Observable<ResponseReceipt> upload(TableRecord table, String actionId, String deviceId, double latitude, double longitude){
        return api.upload(table, actionId, deviceId, latitude, longitude);
    }

    public Observable<ResponseImage> uploadImage(String tableId, MediaBody body, String deviceId){
        return api.uploadImage(body, tableId, deviceId);
    }


    public Observable<Layout> layout(Layout layout, double latitude, double longitude, String deviceId){
        return Observable.zip (
                api.columns(layout.getExternalID() + "|" + layout.getTable().getId(), deviceId, latitude, longitude)
                        .onErrorReturn(new Func1<Throwable, Layout>() {
                            @Override
                            public Layout call(Throwable throwable) {

                                SyncUtil.getInstance().setError(throwable.getMessage());
                                return DummyLayout();
                            }
                        }),
                api.records(layout.getExternalID() + "|" + layout.getTable().getId(), deviceId, latitude, longitude)
                        .onErrorReturn(new Func1<Throwable, TableRecord>() {
                            @Override
                            public TableRecord call(Throwable throwable) {

                                SyncUtil.getInstance().setError(throwable.getMessage());
                                return DummyTableRecord();
                            }
                        }),
                new Func2<Layout, TableRecord, Layout>() {
                    @Override
                    public Layout call(Layout layout, TableRecord tableRecord) {
                        layout.getTable().setRecords(tableRecord.getRecords());
                        return layout;
                    }
                });
    }

    public Observable<Void> sendLocation (LocationModel data) {
        return api.sendLocation(data);
    }

    private TableRecord DummyTableRecord() {
        TableRecord record = new TableRecord();
        List<Record> records = new ArrayList<Record>();
        Record rec = new Record();
        rec.setExternalId("1");

        Column clave = new Column();
        clave.setId("OM_MOVILNOVEDAD_CLAVE");

        Field field = new Field();
        field.setValue("1");
        field.setColumn(clave);

        List<Field> fields = new ArrayList<Field>();
        fields.add(field);
        rec.setFields(fields);
        records.add(rec);
        record.setRecords(records);
        return record;
    }

    private Layout DummyLayout() {
        Layout layout = new Layout();
        Column column = new Column();
        List<LayoutColumn> columns = new ArrayList<>();

        columns.add(new LayoutColumn(layout, column));
        layout.setColumns(columns);
        Table table = new Table();
        table.setId("1");
        layout.setTable(table);
        return layout;
    }
}
