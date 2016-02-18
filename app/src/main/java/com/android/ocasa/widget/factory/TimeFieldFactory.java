package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldTimeView;

/**
 * Created by ignacio on 04/02/16.
 */
public class TimeFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field) {
        FieldTimeView text = (FieldTimeView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_time, container, false);

        text.setValue(field.getValue());

        return text;
    }
}
