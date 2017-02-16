package com.android.ocasa.httpmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 16/02/17.
 */

public class ControlResponse {

    @SerializedName("Imei")
    private String imei;

    @SerializedName("Count")
    private int count;

    @SerializedName("Status")
    private boolean status;

    @SerializedName("Description")
    private String description;

    @SerializedName("Tipo")
    private String type;


    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
