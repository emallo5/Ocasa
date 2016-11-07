package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class ColumnDAO extends GenericDAOImpl<Column, String> {

    public ColumnDAO(Context context) {
        super(Column.class, context);
    }

    public List<Column> findColumnsForTable(String tableId){

        try {
            return dao.queryBuilder().where().eq("table_id", tableId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Column> findColumnsForLayout(String layoutId, String tableId){

        try {
            QueryBuilder<Column, String> columnQuery = dao.queryBuilder();

            QueryBuilder<LayoutColumn, Long> layoutColumnQuery = new LayoutColumnDAO(context).getDao().queryBuilder();

            QueryBuilder<Layout, Long> layoutQuery = new LayoutDAO(context).getDao().queryBuilder();
            layoutQuery.where().eq("table_id", tableId).and().eq("externalID", layoutId);

            layoutColumnQuery.join(layoutQuery);
            columnQuery.join(layoutColumnQuery);

            return columnQuery.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Column> findLogicColumnsForTable(String tableId){

        try {
            return dao.queryBuilder().where().eq("table_id", tableId).and().eq("logic", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Column> findLogicColumnsForLayoutAndTable(String layoutId, String tableId){

        try {
            QueryBuilder<Column, String> columnQuery = dao.queryBuilder();
            columnQuery.where().eq("logic", true);

            QueryBuilder<LayoutColumn, Long> layoutColumnQuery = new LayoutColumnDAO(context).getDao().queryBuilder();

            QueryBuilder<Layout, Long> layoutQuery = new LayoutDAO(context).getDao().queryBuilder();
            layoutQuery.where().eq("table_id", tableId).and().eq("externalID", layoutId);

            layoutColumnQuery.join(layoutQuery);
            columnQuery.join(layoutColumnQuery);

            return columnQuery.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Column findPrimaryKeyColumnForTable(String layoutId, String tableId){

        try {
        QueryBuilder<Column, String> columnQuery = dao.queryBuilder();
        columnQuery.where().eq("primaryKey", true);

        QueryBuilder<LayoutColumn, Long> layoutColumnQuery = new LayoutColumnDAO(context).getDao().queryBuilder();

        QueryBuilder<Layout, Long> layoutQuery = new LayoutDAO(context).getDao().queryBuilder();
        layoutQuery.where().eq("table_id", tableId).and().eq("externalID", layoutId);

        layoutColumnQuery.join(layoutQuery);
        columnQuery.join(layoutColumnQuery);

            return columnQuery.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
