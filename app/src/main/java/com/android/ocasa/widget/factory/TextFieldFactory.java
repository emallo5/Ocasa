package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;

/**
 * Created by ignacio on 28/01/16.
 */
public class TextFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field) {

        EditText text = (EditText) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_text, container, false);

        text.setText(field.getValue());
        text.setHint(field.getColumn().getName());

        return text;
    }
}
