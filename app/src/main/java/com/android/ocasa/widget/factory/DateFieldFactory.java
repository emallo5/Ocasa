package com.android.ocasa.widget.factory;

import android.nfc.FormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldDateView;
import com.android.ocasa.widget.TextFieldView;

import java.util.Date;

/**
 * Created by ignacio on 01/02/16.
 */
public class DateFieldFactory extends FieldViewFactory {

//    @Override
//    public View createView(ViewGroup container, Field field, boolean isEditMode) {
//
//        FieldDateView dateField =
//                (FieldDateView) createView(container, field.getValue(), field.getColumn().getName(), isEditMode);
//
//        initField(dateField.getField(), field, isEditMode);
//
//        TextView input = dateField.getField().getText();
//        input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar, 0, 0, 0);
//        input.setCompoundDrawablePadding(container.getContext().getResources().getDimensionPixelSize(R.dimen.medium_padding));
//
//        return dateField;
//    }

    @Override
    public View createView(ViewGroup container, String value, String label, boolean isEditMode) {

        FieldDateView dateField = (FieldDateView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_date, container, false);

        dateField.setLabel(label);

        return dateField;
    }

    @Override
    public View createSubView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        return null;
    }

    @Override
    public View createView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        FieldDateView dateField = (FieldDateView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_date, container, false);

        dateField.setLabel(field.getLabel());

        if(isEditMode){
            dateField.getField().getAction().setVisibility(View.INVISIBLE);
        }

        Date date = DateTimeHelper.serverParseDate(field.getValue());

        try {
            dateField.setValue(DateTimeHelper.formatDate(date));
        } catch (FormatException e) {
            e.printStackTrace();
        }

        return dateField;
    }

    public void initField(TextFieldView fieldView, Field field, boolean isEditMode){

        TextView textField = fieldView.getText();

        if(!isEditMode)
            textField.setText(field.getValue());

        textField.setMaxEms(field.getColumn().getLength());

        if(!field.getColumn().isEditable()){
            fieldView.getAction().setVisibility(View.INVISIBLE);
        }
    }
}
