package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;

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
}
