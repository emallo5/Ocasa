package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
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
            return dao.queryBuilder().where().eq("record_id", recordId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Field> findFieldsLogicForRecord(String recordId){

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
