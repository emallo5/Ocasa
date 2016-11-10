package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.IOException;
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

    public List<Field> findVisiblesForRecord(String recordId){

        try {
            QueryBuilder<Field, Long> fieldBuilder = dao.queryBuilder();

            QueryBuilder<Column, String> columnBuilder = new ColumnDAO(context).getDao().queryBuilder();
            columnBuilder.where().eq("visible", true);

            fieldBuilder.join(columnBuilder);
            fieldBuilder.where().eq("record_id", recordId);

            return fieldBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Field> findVisiblesForRecordAndLayout(String recordId, String layoutId){

        try {
            QueryBuilder<Field, Long> fieldBuilder = dao.queryBuilder();

            QueryBuilder<Column, String> columnBuilder = new ColumnDAO(context).getDao().queryBuilder();
            columnBuilder.where().eq("visible", true);

            QueryBuilder<LayoutColumn, Long> layoutColumnBuilder = new LayoutColumnDAO(context).getDao().queryBuilder();

            QueryBuilder<Layout, Long> layoutBuilder = new LayoutDAO(context).getDao().queryBuilder();
            layoutBuilder.where().eq("layout_id", layoutId);

            layoutBuilder.join(layoutColumnBuilder);

            columnBuilder.join(layoutBuilder);

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
            columnBuilder.orderBy("order", true);

            fieldBuilder.join(columnBuilder);

            return fieldBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
