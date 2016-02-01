package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Application;

/**
 * Created by ignacio on 14/01/16.
 */
public class ApplicationDAO extends GenericDAOImpl<Application, String> {

    public ApplicationDAO(Context context) {
        super(Application.class, context);
    }
}
