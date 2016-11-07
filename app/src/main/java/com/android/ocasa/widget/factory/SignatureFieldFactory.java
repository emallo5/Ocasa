package com.android.ocasa.widget.factory;

import android.nfc.FormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldSignatureView;

/**
 * Created by ignacio on 20/10/16.
 */

public class SignatureFieldFactory extends FieldViewFactory {

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

        FieldSignatureView signature =
                (FieldSignatureView) LayoutInflater.from(container.getContext()).inflate(R.layout.field_signature, container, false);

        try {
            signature.setValue(field.getValue());
        } catch (FormatException e) {
            e.printStackTrace();
        }

        return signature;
    }
}
