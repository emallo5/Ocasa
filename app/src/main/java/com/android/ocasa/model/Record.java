package com.android.ocasa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @ForeignCollectionField
    private Collection<ReceiptItem> receipts;

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

    public Collection<ReceiptItem> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<ReceiptItem> receipts) {
        this.receipts = receipts;
    }

    public void addReceipt(ReceiptItem receiptItem){
        if(receipts == null)
            receipts = new ArrayList<>();

        receipts.add(receiptItem);
    }

    public Field getPrimaryKey(){
        for (Field field : fields){
            if(field.getColumn().isPrimaryKey()){
                return field;
            }
        }

        return null;
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

    public Field findField(long fieldId){

        for (Field field : fields){
            if(field.getId() == fieldId){
                return field;
            }
        }

        return null;
    }

    public List<Field> getLogicFields(){

        List<Field> logicFields = new ArrayList<>();

        for (Field field : fields){
            if(field.getColumn().isLogic())
                logicFields.add(field);
        }

        return logicFields;
    }
}
