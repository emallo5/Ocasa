package com.android.ocasa.httpmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 16/02/17.
 */

public class ControlBody {

    @SerializedName("imei")
    private String imei;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
