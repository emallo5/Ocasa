package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordDAO extends GenericDAOImpl<Record, Long> {

    public RecordDAO(Context context) {
        super(Record.class, context);
    }

    public List<Record> findForTable(String tableId){

        try {
            return dao.queryBuilder().where().eq("table_id", tableId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Record> findForTableAndQuery(String tableId, String query, long[] excludeIds){

        try {
            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();

            if (query != null){
                QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();
                fieldDao.where().like("value", "%" + query + "%");
                recordQuery.join(fieldDao);
            }

            Where<Record, Long> where = recordQuery.where();
            where.eq("table_id", tableId);

            if(excludeIds != null){
                where.and();
                where.notIn("id", Arrays.asList(ArrayUtils.toObject((excludeIds))));
            }

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

//    public List<Record> findForReceipt(long receiptId){
//        try {
//            return dao.queryBuilder().where().eq("receipt_id", receiptId).query();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public Record findForColumnAndValue(String columnId, String value){

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

    public Record findForTableAndValueId(String tableId, String id){

        try {
            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();

            QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();

            fieldDao.where().eq("value", id);

            QueryBuilder<Column, String> columnDao = new ColumnDAO(context).getDao().queryBuilder();

            columnDao.where().eq("primaryKey", true);

            fieldDao.join(columnDao);

            recordQuery.join(fieldDao);

            recordQuery.where().eq("table_id", tableId);

            return recordQuery.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteForTable(String tableId){
        try {
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("table_id", tableId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
