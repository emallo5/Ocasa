package com.android.ocasa.sync;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.MenuService;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.service.TableService;
import com.android.volley.VolleyError;

import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class SyncService extends Service {

    public static final String EXTRA_SYNC = "sync";
    public static final String EXTRA_ID = "id";

    public static final int MENU_SYNC = 1;
    public static final int TABLE_SYNC = 2;
    public static final int RECORD_SYNC = 3;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int sync = intent.getIntExtra(EXTRA_SYNC, 0);

        switch (sync){
            case MENU_SYNC:
                new MenuSynchronizer(this, startId).performSync(intent.getExtras());
                break;
            case TABLE_SYNC:
                new TableSynchronizer(this, startId).performSync(intent.getExtras());
                break;
            case RECORD_SYNC:
                new RecordSynchronizer(this, startId).performSync(intent.getExtras());
                break;
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MenuSynchronizer extends Synchronizer{

        public MenuSynchronizer(SyncService syncService, int serviceStartId) {
            super(syncService, serviceStartId);
        }

        @Override
        public void performSync(Bundle extras) {
            new MenuService(SyncService.this).requestMenu(new MenuService.SaveMenuResponseCallback(SyncService.this) {
                @Override
                public void onSuccess(Menu response) {
                    super.onSuccess(response);
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    super.onError(error);
                    finish();
                }
            });
        }
    }

    public class TableSynchronizer extends Synchronizer{

        public TableSynchronizer(SyncService syncService, int serviceStartId) {
            super(syncService, serviceStartId);
        }

        @Override
        public void performSync(Bundle extras) {

            String tableId = extras.getString(EXTRA_ID);

            new TableService(SyncService.this)
                    .syncTable(tableId, new TableService.SaveTableResponseCallback(SyncService.this, tableId) {
                        @Override
                        public void onSuccess(HttpTable response) {
                            super.onSuccess(response);
                            finish();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            super.onError(error);
                            finish();
                        }
                    });
        }
    }

    public class RecordSynchronizer extends Synchronizer{

        public RecordSynchronizer(SyncService syncService, int serviceStartId) {
            super(syncService, serviceStartId);
        }

        @Override
        public void performSync(Bundle extras) {

            String tableId = extras.getString(EXTRA_ID);

            List<Record> records = new RecordDAO(SyncService.this).findForTable(tableId);

            if(records != null && !records.isEmpty()) {
                finish();
                return;
            }

            new RecordService(SyncService.this)
                    .syncRecord(tableId, new RecordService.SaveRecordResponseCallback(SyncService.this, tableId){

                        @Override
                        public void onSuccess(TableRecord response) {
                            super.onSuccess(response);
                            finish();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            super.onError(error);
                            finish();
                        }
                    });
        }
    }
}
