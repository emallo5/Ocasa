package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldListView;

/**
 * Created by ignacio on 25/02/16.
 */
public class ListFieldFactory extends FieldViewFactory{

//    @Override
//    public View createView(ViewGroup container, Field field, boolean isEditMode) {
//
//        FieldListView view = (FieldListView) LayoutInflater.from(container.getContext()).inflate(R.layout.field_list, container, false);
//
//        view.setLabel(field.getColumn().getName());
//
//        view.setValue(field.getValue());
//
//        return view;
//    }

    @Override
    public View createView(ViewGroup container, String value, String label, boolean isEditMode) {
        return null;
    }

    @Override
    public View createSubView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        return null;
    }

    @Override
    public View createView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        return null;
    }

}
