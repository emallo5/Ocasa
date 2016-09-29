package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordDAO extends GenericDAOImpl<Record, Long> {

    private static RecordDAO instance;

    public static RecordDAO getInstance(Context context){
        if(instance == null)
            instance = new RecordDAO(context.getApplicationContext());

        return instance;
    }

    private RecordDAO(Context context) {
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

    public Record findByExternalId(String id){
        try {
            return dao.queryBuilder().where().eq("externalId", id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Record> findForTableAndQuery(String tableId, String query, long[] excludeIds){

        try {
            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();

            Where<Record, Long> where = recordQuery.where();

            if (query != null){
                recordQuery.selectRaw("records.id", "fields.id", "fields.value", "fields.column_id", "columns.name", "columns.visible", "columns.logic", "columns.primaryKey", "columns.highlight","'columns'.'order'");
                where.like("concatValues", "%" + query.toLowerCase() + "%")
                        .and()
                        .eq("table_id", tableId);

                if(excludeIds != null && excludeIds.length >  0){
                    where.and();
                    where.notIn("id", Arrays.asList(ArrayUtils.toObject((excludeIds))));
                }

                QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();
                fieldDao.selectColumns("id", "value","column_id");

                QueryBuilder<Column, String> columnDao = new ColumnDAO(context).getDao().queryBuilder();
                recordQuery.orderByRaw("records.id,'columns'.'order'");

                fieldDao.join(columnDao);

                recordQuery.join(fieldDao);

                List<Record> records = new ArrayList<>();

                GenericRawResults<String[]> rawResults = dao.queryRaw(recordQuery.prepareStatementString());

                Record record = null;

                List<Field> fields = null;

                List<String[]> results = rawResults.getResults();

                for (int index = 0; index < results.size(); index++) {

                    String[] resultArray = results.get(index);

                    if(record == null || record.getId() != Long.parseLong(resultArray[0])){
                        record = new Record();
                        record.setId(Long.parseLong(resultArray[0]));

                        records.add(record);

                        fields = new ArrayList<>();
                        record.setFields(fields);
                    }

                    Field field = new Field();
                    field.setId(Integer.parseInt(resultArray[1]));
                    field.setValue(resultArray[2]);

                    Column column = new Column();
                    column.setId(resultArray[3]);
                    column.setName(resultArray[4]);
                    column.setVisible(Integer.parseInt(resultArray[5]) == 1);
                    column.setLogic(Integer.parseInt(resultArray[6]) == 1);
                    column.setPrimaryKey(Integer.parseInt(resultArray[7]) == 1);
                    column.setHighlight(Integer.parseInt(resultArray[8]) == 1);

                    field.setColumn(column);

                    fields.add(field);
                }

                rawResults.close();

                return records;
            }

            recordQuery.selectRaw("records.id", "fields.id", "fields.value", "fields.column_id", "columns.name", "columns.visible", "columns.logic", "columns.primaryKey", "columns.highlight","'columns'.'order'");
            where.eq("table_id", tableId);

            if(excludeIds != null && excludeIds.length >  0){
                where.and();
                where.notIn("id", Arrays.asList(ArrayUtils.toObject((excludeIds))));
            }

            QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();

            QueryBuilder<Column, String> columnDao = new ColumnDAO(context).getDao().queryBuilder();
            recordQuery.orderByRaw("records.id,'columns'.'order'");

            fieldDao.join(columnDao);

            recordQuery.join(fieldDao);

            List<Record> records = new ArrayList<>();

            GenericRawResults<String[]> rawResults = dao.queryRaw(recordQuery.prepareStatementString());

            Record record = null;

            List<Field> fields = null;

            List<String[]> results = rawResults.getResults();

            for (int index = 0; index < results.size(); index++) {

                String[] resultArray = results.get(index);

                if(record == null || record.getId() != Long.parseLong(resultArray[0])){
                    record = new Record();
                    record.setId(Long.parseLong(resultArray[0]));

                    records.add(record);

                    fields = new ArrayList<>();
                    record.setFields(fields);
                }

                Field field = new Field();
                field.setId(Integer.parseInt(resultArray[1]));
                field.setValue(resultArray[2]);

                Column column = new Column();
                column.setId(resultArray[3]);
                column.setName(resultArray[4]);
                column.setVisible(Integer.parseInt(resultArray[5]) == 1);
                column.setLogic(Integer.parseInt(resultArray[6]) == 1);
                column.setPrimaryKey(Integer.parseInt(resultArray[7]) == 1);
                column.setHighlight(Integer.parseInt(resultArray[8]) == 1);

                field.setColumn(column);

                fields.add(field);
            }

            rawResults.close();

            return records;

            /*if(excludeIds != null){
//                where.and();
//
                where.notIn("id", Arrays.asList(ArrayUtils.toObject((excludeIds))));
            }*/

//            recordQuery.groupBy("id");



            //return recordQuery.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Record> findAvailablesForTableAndReceipt(String tableId, long receiptId){

        try {

            QueryBuilder<Record, Long> recordQuery = dao.queryBuilder();
            recordQuery.selectRaw("records.id", "fields.id", "fields.value", "fields.column_id", "columns.logic",  "columns.visible", "columns.primaryKey", "columns.highlight","'columns'.'order'");
            recordQuery.where().eq("table_id", tableId);

            QueryBuilder<ReceiptItem, Long> receiptItemQuery = new ReceiptItemDAO(context).getDao().queryBuilder();

            receiptItemQuery.where().raw("`records`.`table_id` = '" + tableId +
                    "' AND ((`receipt_item`.`record_id` NOT IN (SELECT record_id FROM receipt_item WHERE `receipt_item`.receipt_id == " +
                    receiptId + ") OR 'receipt_item'.receipt_id IS NULL))");

            recordQuery.leftJoin(receiptItemQuery);

            QueryBuilder<Field, Long> fieldDao = new FieldDAO(context).getDao().queryBuilder();

            QueryBuilder<Column, String> columnDao = new ColumnDAO(context).getDao().queryBuilder();
            recordQuery.orderByRaw("records.id,'columns'.'order'");
            recordQuery.groupByRaw("fields.id");

            fieldDao.join(columnDao);

            recordQuery.join(fieldDao);

            List<Record> records = new ArrayList<>();

            GenericRawResults<String[]> rawResults = dao.queryRaw(recordQuery.prepareStatementString());

            Record record = null;

            List<Field> fields = null;

            List<String[]> results = rawResults.getResults();

            for (int index = 0; index < results.size(); index++) {

                String[] resultArray = results.get(index);

                if(record == null || record.getId() != Long.parseLong(resultArray[0])){
                    record = new Record();
                    record.setId(Long.parseLong(resultArray[0]));

                    records.add(record);

                    fields = new ArrayList<>();
                    record.setFields(fields);
                }

                Field field = new Field();
                field.setId(Integer.parseInt(resultArray[1]));
                field.setValue(resultArray[2]);

                Column column = new Column();
                column.setId(resultArray[3]);
                column.setLogic(Integer.parseInt(resultArray[4]) == 1);
                column.setVisible(Integer.parseInt(resultArray[5]) == 1);
                column.setPrimaryKey(Integer.parseInt(resultArray[6]) == 1);
                column.setHighlight(Integer.parseInt(resultArray[7]) == 1);

                field.setColumn(column);

                fields.add(field);
            }

            rawResults.close();

            return records;
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
