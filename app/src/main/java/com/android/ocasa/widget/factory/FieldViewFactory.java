package com.android.ocasa.widget.factory;

import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.model.Field;

/**
 * Created by ignacio on 28/01/16.
 */
public abstract class FieldViewFactory {

    public abstract View createView(ViewGroup container, Field field);

}
