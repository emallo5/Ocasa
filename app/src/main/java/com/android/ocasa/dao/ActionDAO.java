package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Action;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ActionDAO extends GenericDAOImpl<Action, String> {

    public ActionDAO(Context context) {
        super(Action.class, context);
    }
}
