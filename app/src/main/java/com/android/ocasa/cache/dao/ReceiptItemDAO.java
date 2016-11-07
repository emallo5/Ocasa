package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.ReceiptItem;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class ReceiptItemDAO extends GenericDAOImpl<ReceiptItem, Long> {

    public ReceiptItemDAO(Context context) {
        super(ReceiptItem.class, context);
    }

    public int countItemsForReceipt(long receiptId){
        try {
            return (int) dao.queryBuilder().where().eq("receipt_id", receiptId).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<ReceiptItem> findForReceipt(long receiptId){
        try {
            return dao.queryBuilder().orderBy("id", false).where().eq("receipt_id", receiptId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteForReceipt(long receiptId){

        try {
            DeleteBuilder<ReceiptItem, Long> delete = dao.deleteBuilder();
            delete.where().eq("receipt_id", receiptId);
            dao.delete(delete.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
