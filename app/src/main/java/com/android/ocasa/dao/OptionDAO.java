package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Option;

/**
 * Created by ignacio on 01/02/16.
 */
public class OptionDAO extends GenericDAOImpl<Option, String> {

    public OptionDAO(Context context) {
        super(Option.class, context);
    }
}
