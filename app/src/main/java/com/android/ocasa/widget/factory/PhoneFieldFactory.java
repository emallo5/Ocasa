package com.android.ocasa.widget.factory;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.ocasa.R;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldPhoneView;

/**
 * Created by ignacio on 04/02/16.
 */
public class PhoneFieldFactory extends FieldViewFactory {
//    @Override
//    public View createView(ViewGroup container, Field field, boolean isEditMode) {
//
//        FieldPhoneView text = (FieldPhoneView) LayoutInflater
//                .from(container.getContext()).inflate(R.layout.field_phone, container, false);
//
//        text.setLabel(field.getColumn().getName());
//
//        EditText textField = text.getField().getInput();
//        textField.setInputType(InputType.TYPE_CLASS_PHONE);
//
//        if(!isEditMode)
//            textField.setText(field.getValue());
//
//        textField.setHint(field.getColumn().getName());
//
//        return text;
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
