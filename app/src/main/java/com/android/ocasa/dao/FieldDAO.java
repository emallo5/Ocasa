package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class FieldDAO extends GenericDAOImpl<Field, Long> {

    public FieldDAO(Context context) {
        super(Field.class, context);
    }

    public List<Field> findFieldsForRecord(String recordId){

        try {
            List<Field> fields = new ArrayList<>();

            GenericRawResults<String[]> rawResults =
                    dao.queryRaw("SELECT id,value,column_id FROM fields where record_id=" + recordId);

            List<String[]> results = rawResults.getResults();

            for (int index = 0; index < results.size(); index++) {
                String[] resultArray = results.get(index);

                Field field = new Field();

                field.setId(Integer.parseInt(resultArray[0]));
                field.setValue(resultArray[1]);

                Column column = new Column();
                column.setId(resultArray[2]);

                field.setColumn(column);

                fields.add(field);
            }
            rawResults.close();

            return fields;


//            return dao.queryBuilder().where().eq("record_id", recordId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Field> findLogicsForRecord(String recordId){

        try {
            QueryBuilder<Field, Long> fieldBuilder = dao.queryBuilder();

            QueryBuilder<Column, String> columnBuilder = new ColumnDAO(context).getDao().queryBuilder();
            columnBuilder.where().eq("logic", true);

            fieldBuilder.join(columnBuilder);
            fieldBuilder.where().eq("record_id", recordId);

            return fieldBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Field> findForAvailableColumns(long recordId, ArrayList<String> availableColumns){

        try {
            QueryBuilder<Field, Long> fieldBuilder = dao.queryBuilder();

            QueryBuilder<Column, String> columnBuilder = new ColumnDAO(context).getDao().queryBuilder();
            columnBuilder.where().in("id", availableColumns);

            fieldBuilder.join(columnBuilder);
            fieldBuilder.where().eq("record_id", recordId);

            return fieldBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Field> findForReceipt(long receiptId){

        try {
            QueryBuilder<Field, Long> fieldBuilder = dao.queryBuilder();
            fieldBuilder.where().eq("receipt_id", receiptId);

            QueryBuilder<Column, String> columnBuilder = new ColumnDAO(context).getDao().queryBuilder();
            columnBuilder.orderBy("id", false);

            fieldBuilder.join(columnBuilder);

            return fieldBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteFieldsForTable(String tableId){
        try {
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("table_id", tableId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFieldsForRecord(int recordId){
        try {
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("record_id", recordId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
