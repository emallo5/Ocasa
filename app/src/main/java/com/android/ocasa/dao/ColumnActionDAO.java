package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.ColumnAction;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ColumnActionDAO extends GenericDAOImpl<ColumnAction, Integer> {

    public ColumnActionDAO(Context context) {
        super(ColumnAction.class, context);
    }

    public List<ColumnAction> findColumnsForActionAndType(String actionId, ColumnAction.ColumnActionType type){

        try {
            return dao.queryBuilder().where().eq("action_id", actionId).and().eq("type", type).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteForActionId(String actionId){
        try {
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("action_id", actionId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
