package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Record;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordDAO extends GenericDAOImpl<Record, Integer> {

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
}
