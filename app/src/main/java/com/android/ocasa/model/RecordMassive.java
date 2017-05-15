package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by leandro on 15/5/17.
 */

public class RecordMassive {

    @SerializedName("Id")
    String id;

    @SerializedName("Fields")
    ArrayList<MassiveColumn> fields = new ArrayList<>();

    @SerializedName("Codes")
    ArrayList<String> codes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<MassiveColumn> getFields() {
        return fields;
    }

    public void setFields(ArrayList<MassiveColumn> fields) {
        this.fields = fields;
    }

    public ArrayList<String> getCodes() {
        return codes;
    }

    public void setCodes(ArrayList<String> codes) {
        this.codes = codes;
    }

    public void addCode(String code) {
        codes.add(code);
    }

    public void addColumn(MassiveColumn column) {
        fields.add(column);
    }

    public boolean isNotEmpty() {
        return codes.size() > 0;
    }
}
