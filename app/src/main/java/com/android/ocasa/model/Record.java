package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by ignacio on 26/01/16.
 */
@DatabaseTable(tableName = "records")
public class Record {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Table table;

    @ForeignCollectionField(eager = true)
    private Collection<Field> fields;

    @DatabaseField(foreign = true)
    private Receipt receipt;

    public Record() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public void setFields(Collection<Field> fields) {
        this.fields = fields;
    }

    public void updateField(String columnId, String value){

        for (Field field : fields){
            if(field.getColumn().getId().equalsIgnoreCase(columnId)){
                field.setValue(value);
                break;
            }
        }
    }

    public Field getFieldForColumn(String columnId){

        for (Field field : fields){
            if(field.getColumn().getId().equalsIgnoreCase(columnId)){
                return field;
            }
        }

        return null;
    }

    public Column getColumnForField(int fieldId){

        for (Field field : fields){
            if(field.getId() == fieldId){
                return field.getColumn();
            }
        }

        return null;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
