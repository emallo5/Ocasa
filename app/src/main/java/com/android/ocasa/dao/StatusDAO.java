package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Status;

/**
 * Created by ignacio on 02/06/16.
 */
public class StatusDAO extends GenericDAOImpl<Status, Long> {

    public StatusDAO(Context context) {
        super(Status.class, context);
    }
}
