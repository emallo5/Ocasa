package com.android.ocasa.widget.factory;

import android.content.Context;
import android.nfc.FormatException;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FormTextFieldView;
import com.android.ocasa.widget.InputFieldView;
import com.android.ocasa.widget.FormInputFieldView;

/**
 * Created by ignacio on 28/01/16.
 */
public class TextFieldFactory extends FieldViewFactory {

    public View createView(ViewGroup container, String value, String label, boolean isEditMode) {

        FormInputFieldView formField = (FormInputFieldView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_input_field, container, false);


        formField.setLabel(label);
        formField.setValue(value);

        /*if(!isEditMode){
            formField.getFieldFromField().getAction().setVisibility(View.INVISIBLE);
        }*/

        return formField;
    }

//    @Override
//    public View createSubView(ViewGroup container, Field field, boolean isEditMode) {
//
//        FormTextFieldView formField = (FormTextFieldView) LayoutInflater
//                .from(container.getContext()).inflate(R.layout.form_text_field, container, false);
//
//        formField.setLabel(field.getColumn().getName());
//
//        if(!isEditMode)
//            try {
//                formField.setValue(field.getValue());
//            } catch (FormatException e) {
//                e.printStackTrace();
//            }
//
//        formField.getFieldFromField().getAction().setVisibility(View.GONE);
//
//        return formField;
//    }

    @Override
    public View createSubView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        FormTextFieldView formField = (FormTextFieldView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_sub_text_field, container, false);

        formField.getLabel().setVisibility(View.GONE);
        //formField.setLabel(field.getLabel());

        if(!isEditMode)
            try {
                formField.setValue(field.getValue());
            } catch (FormatException e) {
                e.printStackTrace();
            }

        formField.getField().getAction().setVisibility(View.GONE);

        return formField;
    }

    public View createSubView(ViewGroup container, String value, String label, boolean isEditMode) {
        FormTextFieldView formField = (FormTextFieldView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_text_field, container, false);

        formField.setLabel(label);

        if(!isEditMode)
            try {
                formField.setValue(value);
            } catch (FormatException e) {
                e.printStackTrace();
            }

        formField.getField().getAction().setVisibility(View.GONE);

        return formField;
    }

    @Override
    public View createView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        FormInputFieldView formField = (FormInputFieldView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_input_field, container, false);


        formField.setLabel(field.getLabel());
        formField.setValue(field.getValue());

        if(!field.isEditable()) {
            formField.getField().getInput().setEnabled(false);
            formField.getField().getAction().setVisibility(View.GONE);
        }

        // agrego para poder ver los campos
        formField.getField().getInput().setTextColor(ContextCompat.getColor(container.getContext(), R.color.material_blue_grey_800));

        if(isEditMode){
            formField.getField().getAction().setVisibility(View.GONE);
        }

        return formField;
    }

    public void initField(InputFieldView fieldView, Field field, boolean isEditMode){

        EditText textField = fieldView.getInput();

        if(!isEditMode)
            textField.setText(field.getValue());

        textField.setMaxEms(field.getColumn().getLength());

        if(!field.getColumn().isEditable()){
            textField.setEnabled(false);
            fieldView.getAction().setVisibility(View.GONE);
        }
    }
}
