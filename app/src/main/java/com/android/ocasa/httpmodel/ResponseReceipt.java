package com.android.ocasa.httpmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 07/02/17.
 */

public class ResponseReceipt {

    @SerializedName("Number")
    private String id;

    @SerializedName("Record")
    private String record;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
