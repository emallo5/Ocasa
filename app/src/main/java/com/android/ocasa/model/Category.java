package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by ignacio on 14/01/16.
 */
@DatabaseTable(tableName = "categories")
public class Category {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = true)
    private Collection<Table> tables;

    @DatabaseField(foreign = true)
    private Application application;

    public Category() {
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

    public Collection<Table> getTables() {
        return tables;
    }

    public void setTables(Collection<Table> tables) {
        this.tables = tables;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
