package com.android.ocasa.widget.factory;

import android.nfc.FormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldDateTimeView;

/**
 * Created by Emiliano Mallo on 09/05/16.
 */
public class DateTimeFieldFactory extends FieldViewFactory {

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
        FieldDateTimeView dateField = (FieldDateTimeView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.form_datetime_field, container, false);

        dateField.setLabel(field.getLabel());
        dateField.getLabel().setVisibility(View.GONE);

        try {
            dateField.setValue(field.getValue());
        } catch (FormatException e) {
            e.printStackTrace();
        }

        if(isEditMode){
            dateField.getAction().setVisibility(View.GONE);
        }

        return dateField;
    }
}
