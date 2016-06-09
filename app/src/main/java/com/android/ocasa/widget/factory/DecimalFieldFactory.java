package com.android.ocasa.widget.factory;

import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FormInputFieldView;

/**
 * Created by ignacio on 10/02/16.
 */
public class DecimalFieldFactory extends TextFieldFactory {

//    @Override
//    public View createView(ViewGroup container, Field field, boolean isEditMode) {
//
//        FormInputFieldView textView = (FormInputFieldView) super.createView(container, field, isEditMode);
//
//        EditText textField = textView.getField().getInput();
//        textField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//
//        return textView;
//    }

    @Override
    public View createSubView(ViewGroup container, String value, String label, boolean isEditMode) {
        return null;
    }

}
