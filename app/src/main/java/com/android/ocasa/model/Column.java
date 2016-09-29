package com.android.ocasa.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Ignacio Oviedo on 26/01/16.
 */
@DatabaseTable(tableName = "columns")
public class Column {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private int order;

    @DatabaseField
    private String name;

    @DatabaseField
    private boolean editable;

    @DatabaseField
    private int length;

    @DatabaseField
    private boolean mandatory;

    @DatabaseField
    private boolean primaryKey;

    @DatabaseField
    private boolean logic;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private FieldType fieldType;

    @DatabaseField(foreign = true)
    private Table table;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Table relationship;

    @DatabaseField
    private boolean highlight;

    @DatabaseField
    private boolean visible;

    @DatabaseField
    private String defaultValue;

    public Column() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isLogic() {
        return logic;
    }

    public void setLogic(boolean logic) {
        this.logic = logic;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getRelationship() {
        return relationship;
    }

    public void setRelationship(Table relationship) {
        this.relationship = relationship;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
