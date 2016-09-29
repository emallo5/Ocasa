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

    @DatabaseField(foreign = true)
    private Category category;

    @ForeignCollectionField
    private Collection<Column> columns;

    @DatabaseField(foreign = true)
    private Table table;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<Column> getColumns() {
        return columns;
    }

    public void setColumns(Collection<Column> columns) {
        this.columns = columns;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
