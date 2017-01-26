package com.android.ocasa.api;

import com.android.ocasa.httpmodel.MediaBody;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.Response;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.session.SessionManager;

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

    public Observable<Layout> layout(Layout layout, double latitude, double longitude, String deviceId){
        return Observable.zip (
                api.columns(layout.getExternalID() + "|" + layout.getTable().getId(), deviceId, latitude, longitude)
                        .onErrorReturn(new Func1<Throwable, Layout>() {
                            @Override
                            public Layout call(Throwable throwable) {

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
                        }),
                api.records(layout.getExternalID() + "|" + layout.getTable().getId(), deviceId, latitude, longitude)
//                        .map(new Func1<TableRecord, TableRecord>() {
//                            @Override
//                            public TableRecord call(TableRecord tableRecord) {
//
//                                if(tableRecord.getRecords() == null){
//                                    List<Record> records = new ArrayList<Record>();
//
//                                    Record rec = new Record();
//                                    rec.setExternalId("1");
//
//                                    Column clave = new Column();
//                                    clave.setId("OM_MOVILNOVEDAD_CLAVE");
//
//                                    Field field = new Field();
//                                    field.setValue("1");
//                                    field.setColumn(clave);
//
//                                    Column equipo = new Column();
//                                    equipo.setId("OM_MOVILNOVEDAD_C_0012");
//
//                                    Field field1 = new Field();
//                                    field1.setValue("Equipo");
//                                    field1.setColumn(equipo);
//
//                                    List<Field> fields = new ArrayList<Field>();
//
//                                    fields.add(field);
//                                    fields.add(field1);
//
//                                    rec.setFields(fields);
//
//                                    records.add(rec);
//
//                                    tableRecord.setRecords(records);
//                                }
//
//                                return tableRecord;
//                            }
//                        })
                        .onErrorReturn(new Func1<Throwable, TableRecord>() {
                            @Override
                            public TableRecord call(Throwable throwable) {

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
                        }),
                new Func2<Layout, TableRecord, Layout>() {
                    @Override
                    public Layout call(Layout layout, TableRecord tableRecord) {
                        layout.getTable().setRecords(tableRecord.getRecords());
                        return layout;
                    }
                });
    }

    public Observable<Receipt> upload(TableRecord table, String actionId, String deviceId, double latitude, double longitude){
        return api.upload(table, actionId, deviceId, latitude, longitude);
    }

    public Observable<Response> uploadImage(String tableId, MediaBody body, String deviceId){
        return api.uploadImage(body, tableId, deviceId);
    }
}
