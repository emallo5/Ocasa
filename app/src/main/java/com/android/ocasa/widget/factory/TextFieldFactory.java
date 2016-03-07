package com.android.ocasa.widget.factory;

import android.nfc.FormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FormTextFieldView;
import com.android.ocasa.widget.InputFieldView;
import com.android.ocasa.widget.FormInputFieldView;

/**
 * Created by ignacio on 28/01/16.
 */
public class TextFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field, boolean isEditMode) {

        FormInputFieldView formField = (FormInputFieldView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_input_field, container, false);

        formField.setLabel(field.getColumn().getName());

        initField(formField.getField(), field, isEditMode);

        return formField;
    }

    @Override
    public View createSubView(ViewGroup container, Field field, boolean isEditMode) {

        FormTextFieldView formField = (FormTextFieldView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_text_field, container, false);

        formField.setLabel(field.getColumn().getName());

        if(!isEditMode)
            try {
                formField.setValue(field.getValue());
            } catch (FormatException e) {
                e.printStackTrace();
            }

        formField.getField().getAction().setVisibility(View.INVISIBLE);

        return formField;
    }

    public void initField(InputFieldView fieldView, Field field, boolean isEditMode){

        EditText textField = fieldView.getInput();

        if(!isEditMode)
            textField.setText(field.getValue());

        textField.setMaxEms(field.getColumn().getLength());

        if(!field.getColumn().isEditable()){
            textField.setEnabled(false);
            fieldView.getAction().setVisibility(View.INVISIBLE);
        }
    }
}
