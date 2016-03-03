package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by ignacio on 22/02/16.
 */
@DatabaseTable(tableName = "receipts")
public class Receipt {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private int number;

    @ForeignCollectionField
    private Collection<Record> records;

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

    public Collection<Record> getRecords() {
        return records;
    }

    public void setRecords(Collection<Record> records) {
        this.records = records;
    }
}
