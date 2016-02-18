package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldTextView;

/**
 * Created by ignacio on 28/01/16.
 */
public class TextFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field) {

        FieldTextView text = (FieldTextView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_text, container, false);

        EditText textField = text.getTextField();
        textField.setText(field.getValue());
        textField.setHint(field.getColumn().getName());
        textField.setMaxEms(field.getColumn().getLength());

        if(!field.getColumn().isEditable()){
            textField.setEnabled(false);
            text.getQrButton().setVisibility(View.INVISIBLE);
        }

        return text;
    }
}
