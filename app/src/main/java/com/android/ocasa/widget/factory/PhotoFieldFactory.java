package com.android.ocasa.widget.factory;

import android.nfc.FormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldPhotoView;

/**
 * Created by ignacio on 20/10/16.
 */

public class PhotoFieldFactory extends FieldViewFactory {

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

        FieldPhotoView photo =
                (FieldPhotoView) LayoutInflater.from(container.getContext()).inflate(R.layout.field_photo, container, false);

        try {
            photo.setValue(field.getValue());
        } catch (FormatException e) {
            e.printStackTrace();
        }

        return photo;
    }
}
