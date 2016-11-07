package com.android.ocasa.cache.dao;

import android.content.Context;

import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ignacio on 11/02/16.
 */
public class HistoryDAO extends GenericDAOImpl<History, Integer> {

    public HistoryDAO(Context context) {
        super(History.class, context);
    }

    public List<History> findHistoricalForColumnAndRecord(String columnId, long recordId){
        try {
            QueryBuilder<History, Integer> historyBuilder = dao.queryBuilder();

            QueryBuilder<Field, Long> fieldBuilder = new FieldDAO(context).getDao().queryBuilder();
            fieldBuilder.where().eq("column_id", columnId).and().eq("record_id", recordId);

            historyBuilder.join(fieldBuilder);
            historyBuilder.orderBy("systemDate", false);
            return historyBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public History findForReceiptAndField(String receiptId, String fieldId){
        try {
            return dao.queryBuilder().where()
                    .eq("receipt_id", receiptId)
                    .and()
                    .eq("field_id", fieldId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
