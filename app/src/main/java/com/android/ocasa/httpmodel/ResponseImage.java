package com.android.ocasa.httpmodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 21/12/16.
 */

public class ResponseImage {

    @SerializedName("Status")
    int status;

    @SerializedName("Description")
    String description;

    @SerializedName("Tipo")
    String tipo;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
