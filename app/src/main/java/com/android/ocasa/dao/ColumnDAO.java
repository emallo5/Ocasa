package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Column;

/**
 * Created by ignacio on 28/01/16.
 */
public class ColumnDAO extends GenericDAOImpl<Column, String> {

    public ColumnDAO(Context context) {
        super(Column.class, context);
    }
}
