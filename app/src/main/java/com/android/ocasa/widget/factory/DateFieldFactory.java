package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldDateView;

/**
 * Created by ignacio on 01/02/16.
 */
public class DateFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field) {
        FieldDateView text = (FieldDateView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_date, container, false);

        text.setText(field.getValue());

        return text;
    }
}
