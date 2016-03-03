package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.widget.FieldComboView;

/**
 * Created by ignacio on 01/02/16.
 */
public class ComboFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, Field field, boolean isEditMode) {
        FieldComboView text = (FieldComboView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_combo, container, false);

        text.setLabel(field.getColumn().getName());

        text.setValue(field.getValue());

        for (Field relationshipField : field.getRelationship().getFields()){

            View child = relationshipField.getColumn().getFieldType().getFieldFactory().createSubView(text, relationshipField, isEditMode);
            child.setTag(relationshipField.getColumn().getId());

            text.getContainer().addView(child);
        }

        return text;
    }

    @Override
    public View createSubView(ViewGroup container, Field field, boolean isEditMode) {
        return null;
    }
}
