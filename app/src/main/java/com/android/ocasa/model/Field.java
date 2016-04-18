package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ignacio on 26/01/16.
 */
@DatabaseTable(tableName = "fields")
public class Field {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Column column;

    @DatabaseField(foreign = true)
    private Record record;

    @DatabaseField
    private String value;

    private Record relationship;

    @ForeignCollectionField(eager = false)
    private Collection<History> historical;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public Object getFormatValue(){
        return column.getFieldType().format(value);
    }

    public Record getRelationship() {
        return relationship;
    }

    public void setRelationship(Record relationship) {
        this.relationship = relationship;
    }

    public Collection<History> getHistorical() {
        return historical;
    }

    public void setHistorical(Collection<History> historical) {
        this.historical = historical;
    }

    public void addHistory(History history){
        if(historical == null)
            historical = new ArrayList<>();

        historical.add(history);
    }
}
