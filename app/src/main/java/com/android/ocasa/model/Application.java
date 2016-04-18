package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ignacio on 14/01/16.
 */
@DatabaseTable(tableName = "applications")
public class Application {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = true, maxEagerForeignCollectionLevel = 2)
    private Collection<Category> categories;

    public Application() {
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

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
