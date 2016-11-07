package com.android.ocasa.httpmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 07/11/16.
 */

public class Archive {

    @SerializedName("Type")
    private String type;

    @SerializedName("Base")
    private String base;

    @SerializedName("Data")
    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
