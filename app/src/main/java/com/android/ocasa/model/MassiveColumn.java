package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leandro on 15/5/17.
 */

public class MassiveColumn {

    @SerializedName("Column_id")
    String id;

    @SerializedName("Value")
    String value;

    public MassiveColumn(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public MassiveColumn () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
