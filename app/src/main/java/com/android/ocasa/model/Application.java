package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ignacio on 14/01/16.
 */
@DatabaseTable(tableName = "applications")
public class Application {

    @SerializedName("Id")
    @DatabaseField(id = true)
    private String id;

    @SerializedName("Name")
    @DatabaseField
    private String name;

    @SerializedName("Categories")
    @ForeignCollectionField(eager = true, maxEagerLevel = 3)
    private Collection<Category> categories;

    @SerializedName("record_color")
    @DatabaseField(defaultValue = "#33BDC2")
    private String recordColor;

    @SerializedName("receipt_color")
    @DatabaseField(defaultValue = "#33BDC2")
    private String receiptColor;

    public Application() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getRecordColor() {
        return recordColor;
    }

    public void setRecordColor(String recordColor) {
        this.recordColor = recordColor;
    }

    public String getReceiptColor() {
        return receiptColor;
    }

    public void setReceiptColor(String receiptColor) {
        this.receiptColor = receiptColor;
    }
}
