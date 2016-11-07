package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.Layout;

import java.sql.SQLException;

/**
 * Created by ignacio on 29/09/16.
 */

public class LayoutDAO extends GenericDAOImpl<Layout, Long> {

    public LayoutDAO(Context context) {
        super(Layout.class, context);
    }

    public Layout findByExternalId(String externalId){
        try {
            return dao.queryBuilder().where().eq("externalID", externalId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
