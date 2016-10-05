package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by ignacio on 29/09/16.
 */
@DatabaseTable(tableName = "layouts")
public class Layout {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String externalID;

    @DatabaseField(foreign = true)
    private Category category;

    @ForeignCollectionField
    private Collection<LayoutColumn> columns;

    @DatabaseField(foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Table table;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<LayoutColumn> getColumns() {
        return columns;
    }

    public void setColumns(Collection<LayoutColumn> columns) {
        this.columns = columns;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }
}
