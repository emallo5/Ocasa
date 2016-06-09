package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.ReceiptItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class ReceiptItemDAO extends GenericDAOImpl<ReceiptItem, Long> {

    public ReceiptItemDAO(Context context) {
        super(ReceiptItem.class, context);
    }

    public List<ReceiptItem> findForReceipt(long receiptId){
        try {
            return dao.queryBuilder().where().eq("receipt_id", receiptId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
