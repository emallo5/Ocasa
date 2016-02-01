package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class FieldDAO extends GenericDAOImpl<Field, Integer> {

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
}
