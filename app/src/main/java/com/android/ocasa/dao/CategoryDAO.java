package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.model.Category;


/**
 * Created by ignacio on 18/01/16.
 */
public class CategoryDAO extends GenericDAOImpl<Category, String> {

    public CategoryDAO(Context context) {
        super(Category.class, context);
    }
}
