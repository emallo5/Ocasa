package com.android.ocasa.widget.factory;

import android.nfc.FormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldDateView;
import com.android.ocasa.widget.FieldTimeView;
import com.android.ocasa.widget.TextFieldView;

import java.util.Date;

/**
 * Ignacio on 04/02/16.
 */
public class TimeFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, FieldViewModel field, boolean isEditMode) {

        FieldTimeView dateField = (FieldTimeView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_time, container, false);

//        dateField.setLabel(field.getColumn().getName());
        dateField.setLabel(field.getLabel());
        try {
            dateField.setValue(DateTimeHelper.formatTime(new Date()));
        } catch (FormatException e) {}

//        TextView input = dateField.getFieldFromField().getText();
//        input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock, 0, 0, 0);
//        input.setCompoundDrawablePadding(container.getContext().getResources().getDimensionPixelSize(R.dimen.medium_padding));

        return dateField;
    }

    @Override
    public View createView(ViewGroup container, String value, String label, boolean isEditMode) {

        FieldTimeView dateField = (FieldTimeView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_time, container, false);

        dateField.setLabel(label);

        return dateField;
    }

    @Override
    public View createSubView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        return null;
    }

}
