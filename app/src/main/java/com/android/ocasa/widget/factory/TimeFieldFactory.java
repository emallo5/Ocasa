package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldTimeView;

/**
 * Created by ignacio on 04/02/16.
 */
public class TimeFieldFactory extends DateFieldFactory {

    @Override
    public View createView(ViewGroup container, Field field, boolean isEditMode) {
        FieldTimeView dateField = (FieldTimeView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_time, container, false);

        dateField.setLabel(field.getColumn().getName());

        initField(dateField.getField(), field, isEditMode);

        TextView input = dateField.getField().getText();
        input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock, 0, 0, 0);
        input.setCompoundDrawablePadding(container.getContext().getResources().getDimensionPixelSize(R.dimen.medium_padding));

        return dateField;
    }
}
