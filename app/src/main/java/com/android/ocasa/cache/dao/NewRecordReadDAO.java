package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.NewRecordRead;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leandro on 8/5/17.
 */

public class NewRecordReadDAO extends GenericDAOImpl<NewRecordRead, String> {

    public NewRecordReadDAO(Context context) {
        super(NewRecordRead.class, context);
    }

    public List<NewRecordRead> findByRecordId(long record) {

        try {
            return dao.queryBuilder().where().eq("record_id", String.valueOf(record)).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
