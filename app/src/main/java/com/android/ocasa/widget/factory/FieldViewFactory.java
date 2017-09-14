package com.android.ocasa.widget.factory;

import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.model.Field;
import com.android.ocasa.viewmodel.FieldViewModel;

/**
 * Created by ignacio on 28/01/16.
 */
public abstract class FieldViewFactory {

    public abstract View createView(ViewGroup container, String value, String label, boolean isEditMode);

    public abstract View createSubView(ViewGroup container, FieldViewModel field, boolean isEditMode);

    public abstract View createView(ViewGroup container, FieldViewModel field, boolean isEditMode);

    public void setLabel(String label) {}
}