package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldPhoneView;

/**
 * Created by ignacio on 04/02/16.
 */
public class PhoneFieldFactory extends FieldViewFactory {
    @Override
    public View createView(ViewGroup container, Field field) {

        FieldPhoneView text = (FieldPhoneView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_phone, container, false);

        EditText textField = text.getTextField();
        textField.setText(field.getValue());
        textField.setHint(field.getColumn().getName());

        return text;
    }
}
