package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
@DatabaseTable(tableName = "actions")
public class Action {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = false)
    private Collection<ColumnAction> columnsHeader;

    @ForeignCollectionField(eager = false)
    private Collection<ColumnAction> columnsDetail;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Table table;

    @DatabaseField(foreign = true)
    private Category category;

    @DatabaseField
    private String order = "";

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

    public Collection<ColumnAction> getColumnsHeader() {
        return columnsHeader;
    }

    public void setColumnsHeader(Collection<ColumnAction> columnsHeader) {
        this.columnsHeader = columnsHeader;
    }

    public Collection<ColumnAction> getColumnsDetail() {
        return columnsDetail;
    }

    public void setColumnsDetail(Collection<ColumnAction> columnsDetail) {
        this.columnsDetail = columnsDetail;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public Column getHeaderColumnForId(String columnId){
        for (ColumnAction columnAction: columnsHeader) {
            Column column = columnAction.getColumn();
            if(column.getId().equals(columnId))
                return column;
        }

        return null;
    }

    public ArrayList<String> getDetailsComlumIds(){
        ArrayList<String> ids = new ArrayList<>();

        for (ColumnAction columnAction : columnsDetail){
            ids.add(columnAction.getColumn().getId());
        }

        return ids;
    }
}
