package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ignacio on 01/02/16.
 */
@DatabaseTable(tableName = "options")
public class Option {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String value;

    @DatabaseField(foreign = true)
    private Column column;

    public Option() {
    }

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

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
