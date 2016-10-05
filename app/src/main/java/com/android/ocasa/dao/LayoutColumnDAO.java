package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.LayoutColumn;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 03/10/16.
 */

public class LayoutColumnDAO extends GenericDAOImpl<LayoutColumn, Long> {

    public LayoutColumnDAO(Context context) {
        super(LayoutColumn.class, context);
    }

    public void deleteForLayout(String layoutId){
//        try {
//            DeleteBuilder deleteBuilder = dao.deleteBuilder();
//            deleteBuilder.where().eq("layout_id", layoutId);
//
//            deleteBuilder.delete();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public List<Column> findLogicColumnsForLayout(String actionId) {

//        SELECT * FROM layout_column INNER JOIN columns ON layout_column.column_id=columns.id WHERE layout_id="MLappl1|1|c_0005|Ingresos" AND logic=1




        return null;
    }
}

