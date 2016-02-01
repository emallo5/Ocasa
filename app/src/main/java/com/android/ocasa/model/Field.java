package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by ignacio on 26/01/16.
 */

public class Field {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Column column;

    @DatabaseField(foreign = true)
    private Record record;

    @DatabaseField
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public Object getFormatValue(){
        return column.getFieldType().format(value);
    }
}
