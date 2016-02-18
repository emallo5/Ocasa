package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ignacio on 11/02/16.
 */
@DatabaseTable(tableName = "historical")
public class History {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String date;

    @DatabaseField
    private String value;

    @DatabaseField(foreign = true)
    private Field field;

    @DatabaseField(foreign = true)
    private User user;

    public History() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
