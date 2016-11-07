package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.LayoutColumn;

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
}

