package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.List;

/**
 * Created by ignacio on 14/01/16.
 */
@DatabaseTable(tableName = "tables")
public class Table {

    static final String TYPE_FTTS = "FTTS";
    static final String TYPE_RCCMP = "RCCMP";
    static final String TYPE_RACMP = "RACMP";
    static final String TYPE_CTTS = "CTTS";
    static final String TYPE_ATTS = "ATTS";

    @SerializedName("Id")
    @DatabaseField(id = true)
    private String id;

    @SerializedName("Name")
    @DatabaseField
    private String name;

    @DatabaseField(foreign = true)
    private Category category;

    @ForeignCollectionField
    private Collection<Record> records;

    @DatabaseField
    private String filters;

    @SerializedName("Columns")
    private List<Column> columns;

    @SerializedName("Type")
    @DatabaseField
    private String type;

    @DatabaseField
    private boolean visible;

    public Table() {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<Record> getRecords() {
        return records;
    }

    public void setRecords(Collection<Record> records) {
        this.records = records;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean canAddNewRecord(){
        return type != null && (type.equalsIgnoreCase(TYPE_ATTS) || type.equalsIgnoreCase(TYPE_RACMP));
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
