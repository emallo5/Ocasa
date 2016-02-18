package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.History;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 11/02/16.
 */
public class HistoryDAO extends GenericDAOImpl<History, Integer> {

    public HistoryDAO(Context context) {
        super(History.class, context);
    }

    public List<History> findHistoricalForField(String fieldId){
        try {
            return dao.queryBuilder().orderBy("date", false).where().eq("field_id", fieldId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
