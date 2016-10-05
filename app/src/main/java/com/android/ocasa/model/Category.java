package com.android.ocasa.model;

import android.support.design.widget.TabLayout;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by ignacio on 14/01/16.
 */
@DatabaseTable(tableName = "categories")
public class Category {

    @SerializedName("Id")
    @DatabaseField(id = true)
    private String id;

    @SerializedName("Name")
    @DatabaseField
    private String name;

    @SerializedName("Visible")
    @DatabaseField
    private boolean isVisible;

    @SerializedName("Tables")
    @ForeignCollectionField(eager = true)
    private Collection<Table> tables;

    @ForeignCollectionField(eager = true)
    private Collection<Layout> layouts;

    @SerializedName("Actions")
    @ForeignCollectionField(eager = true)
    private Collection<Action> actions;

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

    public Collection<Action> getActions() {
        return actions;
    }

    public void setActions(Collection<Action> actions) {
        this.actions = actions;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Collection<Layout> getLayouts() {
        return layouts;
    }

    public void setLayouts(Collection<Layout> layouts) {
        this.layouts = layouts;
    }
}
