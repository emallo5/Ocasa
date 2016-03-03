package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordDAO extends GenericDAOImpl<Record, Long> {

    public RecordDAO(Context context) {
        super(Record.class, context);
    }

    public List<Record> findRecordsForTable(String tableId){

        try {
            return dao.queryBuilder().where().eq("table_id", tableId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Record> findRecordsForTableAndQuery(String tableId, String query){

        try {
            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();


            QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();

            fieldDao.where().like("value", "%" + query + "%");

            recordQuery.join(fieldDao);

            recordQuery.where().eq("table_id", tableId);
            recordQuery.groupBy("id");

            return recordQuery.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Record> findDetailRecords(String tableId, String columnId, String value){

        try {
            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();

            QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();

            fieldDao.where().like("column_id", columnId).and().eq("value", value);

            recordQuery.join(fieldDao);

            recordQuery.where().eq("table_id", tableId);
            recordQuery.groupBy("id");

            return recordQuery.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Record findRecordsForColumnAndValue(String columnId, String value){

        try {
            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();

            QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();

            fieldDao.where().eq("column_id", columnId).and().eq("value", value);

            recordQuery.join(fieldDao);

            return recordQuery.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteRecordsForTable(String tableId){
        try {
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("table_id", tableId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
