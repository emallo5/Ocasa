package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Ignacio Oviedo on 22/02/16.
 */
@DatabaseTable(tableName = "receipts")
public class Receipt {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private int number;

    @ForeignCollectionField
    private Collection<ReceiptItem> items;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Action action;

    @DatabaseField
    private String validityDate;

    public Receipt() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Collection<ReceiptItem> getItems() {
        return items;
    }

    public void setItems(Collection<ReceiptItem> items) {
        this.items = items;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }
}
