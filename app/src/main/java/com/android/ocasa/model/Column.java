package com.android.ocasa.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.w3c.dom.ProcessingInstruction;

import java.util.Collection;

/**
 * Created by ignacio on 26/01/16.
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

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private FieldType fieldType;

    @ForeignCollectionField(eager = true)
    private Collection<Option> options;

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

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Collection<Option> getOptions() {
        return options;
    }

    public void setOptions(Collection<Option> options) {
        this.options = options;
    }
}
