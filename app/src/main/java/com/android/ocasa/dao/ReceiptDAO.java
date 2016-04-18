package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Receipt;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Emiliano Mallo on 28/03/16.
 */
public class ReceiptDAO extends GenericDAOImpl<Receipt,Long> {

    public ReceiptDAO(Context context) {
        super(Receipt.class, context);
    }

    public List<Receipt> findForAction(String actionId){
        try {
            return dao.queryBuilder().where().eq("action_id", actionId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
