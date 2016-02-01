package com.android.ocasa.sync;

import android.os.Bundle;

/**
 * Created by ignacio on 28/01/16.
 */
public abstract class Synchronizer {

    private int serviceStartId;

    private SyncService service;

    public Synchronizer(SyncService syncService, int serviceStartId){
        this.service = syncService;
        this.serviceStartId = serviceStartId;
    }

    public abstract void performSync(Bundle extras);

    public void finish(){
        service.stopSelf(serviceStartId);
    }
}
