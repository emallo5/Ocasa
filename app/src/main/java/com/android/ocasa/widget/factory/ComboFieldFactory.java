package com.android.ocasa.widget.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldComboView;

/**
 * Created by ignacio on 01/02/16.
 */
public class ComboFieldFactory extends FieldViewFactory {

    @Override
    public View createView(ViewGroup container, String value, String label, boolean isEditMode) {
        FieldComboView text = (FieldComboView) LayoutInflater
                .from(container.getContext()).inflate(R.layout.field_combo, container, false);

        text.getLabel().setVisibility(View.GONE);

        text.setLabel(label);
        text.setValue(value);

        return text;
    }

    @Override
    public View createSubView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        return null;
    }


    @Override
    public View createView(ViewGroup container, FieldViewModel field, boolean isEditMode) {
        FieldComboView text =
                (FieldComboView) createView(container, field.getValue(), field.getLabel(), isEditMode);

        text.setEnabled(field.isEditable());

        if(isEditMode)
            text.getAction().setVisibility(View.GONE);

        for (FieldViewModel subField : field.getRelationshipFields()){

            View child = subField.getType().getFieldFactory().createSubView(text, subField, false);
            child.setTag(subField.getTag());

            text.getContainer().addView(child);
        }

        return text;
    }
}
