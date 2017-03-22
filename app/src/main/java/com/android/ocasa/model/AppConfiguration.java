package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leandro on 22/3/17.
 */

public class AppConfiguration {

    @SerializedName("lap_sync_proc")
    private int lapSyncProc = 0;
    @SerializedName("pod_delay")
    private int podDelay = 0;
    @SerializedName("proc_sync_on")
    private boolean syncProcOn = true;
    @SerializedName("proc_location_on")
    private boolean locationProcOn = false;
    @SerializedName("proc_trip_on")
    private boolean tripProcOn = true;
    @SerializedName("lap_trip_proc")
    private int lapTripProc = 0;
    @SerializedName("lap_location_proc")
    private int lapLocationProc = 0;
    @SerializedName("img_compress_percent")
    private int imgCompress = 0;

    public int getLapSyncProc() {
        return lapSyncProc == 0 ? 5 : lapSyncProc;
    }

    public void setLapSyncProc(int lapSyncProc) {
        this.lapSyncProc = lapSyncProc;
    }

    public int getPodDelay() {
        return podDelay == 0 ? 5 : podDelay;
    }

    public void setPodDelay(int podDelay) {
        this.podDelay = podDelay;
    }

    public boolean isSyncProcOn() {
        return syncProcOn;
    }

    public void setSyncProcOn(boolean syncProcOn) {
        this.syncProcOn = syncProcOn;
    }

    public boolean isLocationProcOn() {
        return locationProcOn;
    }

    public void setLocationProcOn(boolean locationProcOn) {
        this.locationProcOn = locationProcOn;
    }

    public boolean isTripProcOn() {
        return tripProcOn;
    }

    public void setTripProcOn(boolean tripProcOn) {
        this.tripProcOn = tripProcOn;
    }

    public int getLapTripProc() {
        return lapTripProc == 0 ? 2 : lapTripProc;
    }

    public void setLapTripProc(int lapTripProc) {
        this.lapTripProc = lapTripProc;
    }

    public int getLapLocationProc() {
        return lapLocationProc == 0 ? 30000 : lapLocationProc*1000;
    }

    public void setLapLocationProc(int lapLocationProc) {
        this.lapLocationProc = lapLocationProc;
    }

    public int getImgCompress() {
        return imgCompress == 0 ? 15 : (100 - imgCompress);
    }

    public void setImgCompress(int imgCompress) {
        this.imgCompress = imgCompress;
    }
}
