package com.android.ocasa.api;

import com.android.ocasa.httpmodel.MediaBody;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.LoginCredentials;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.session.SessionManager;

import java.util.ArrayList;

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
                });
    }

    public Observable<Receipt> upload(TableRecord table, String actionId, String deviceId, double latitude, double longitude){
        return api.upload(table, actionId, deviceId, latitude, longitude);
    }

    public Observable<String> uploadImage(String tableId, MediaBody body, String deviceId){
        return api.uploadImage(body, tableId, deviceId);
    }
}
