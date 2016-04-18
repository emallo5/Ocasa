package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
@DatabaseTable(tableName = "receipt_item")
public class ReceiptItem {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true)
    private Receipt receipt;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Record record;

    public ReceiptItem(){}

    public ReceiptItem(Receipt receipt, Record record){
        this.receipt = receipt;
        this.record = record;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
