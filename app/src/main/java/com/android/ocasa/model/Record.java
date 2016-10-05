package com.android.ocasa.model;

import android.util.Log;

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

    static final String STATUS_CREATED = "CREATED";
    static final String STATUS_UPDATED = "UPDATED";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String externalId;

    @DatabaseField
    private String status;

    @DatabaseField(foreign = true)
    private Table table;

    @ForeignCollectionField(eager = true)
    private Collection<Field> fields;

    @ForeignCollectionField
    private Collection<ReceiptItem> receipts;

    @DatabaseField
    private String concatValues;

    public Record() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

        return new ArrayList<>(fields).get(0);
    }

    public Field getFieldForColumn(String columnId){

        for (Field field : fields){
            if(field.getColumn() != null && field.getColumn().getId().equalsIgnoreCase(columnId)){
                return field;
            }
        }

        return null;
    }

    public Field getFieldForColumnSplit(String columnId){

        String[] id = columnId.split("\\|");

        String searchId = id[id.length -1];

        for (Field field : fields){
            if(field.getColumn() != null && field.getColumn().getId().contains(searchId)){
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

    public List<Field> getVisibleFields(){

        List<Field> logicFields = new ArrayList<>();

        for (Field field : fields){
            if(field.getColumn().isVisible())
                logicFields.add(field);
        }

        return logicFields;
    }

    public void updateStatus(){
        status = STATUS_UPDATED;
    }

    public boolean isNew(){
        return status != null && status.equalsIgnoreCase(STATUS_CREATED);
    }

    public boolean isUpdated(){
        return status != null && status.equalsIgnoreCase(STATUS_UPDATED);
    }

    public void fillConcatValues(){
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            sb.append(field.getValue().toLowerCase());
        }

        concatValues = sb.toString();
    }

    public String getConcatValues() {
        return concatValues;
    }

    public void setConcatValues(String concatValues) {
        this.concatValues = concatValues;
    }
}
