package com.android.ocasa.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
@DatabaseTable(tableName = "columns_action")
public class ColumnAction {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Column column;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    public ColumnActionType type;

    @DatabaseField
    private boolean editable;

    @DatabaseField
    private String defaultValue;

    @DatabaseField
    private String lastValue;

    @DatabaseField(foreign = true)
    private Action action;

    public enum ColumnActionType{
        HEADER,
        DETAIL
    }

    public ColumnAction(){}

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

    public ColumnActionType getType() {
        return type;
    }

    public void setType(ColumnActionType type) {
        this.type = type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
