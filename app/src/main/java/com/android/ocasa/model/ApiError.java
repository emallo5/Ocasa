package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 22/08/16.
 */
public class ApiError {

    @SerializedName("Status")
    private String status;

    @SerializedName("Description")
    private String description;

    @SerializedName("Tipo")
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
