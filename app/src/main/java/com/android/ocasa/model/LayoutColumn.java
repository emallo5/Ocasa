package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ignacio on 03/10/16.
 */
@DatabaseTable(tableName = "layout_column")
public class LayoutColumn {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true)
    private Layout layout;

    @DatabaseField(foreign = true)
    private Column column;

    public LayoutColumn(){}

    public LayoutColumn(Layout layout, Column column){
        this.layout = layout;
        this.column = column;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
