package com.android.ocasa.receipt.item.detailaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.util.InformationDialogFragment;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ignacio on 24/10/16.
 */
public class DetailActionFragment extends FormFragment {

    static String ARG_RECEIPT_ID = "receipt_id";
    static String ARG_RECORD_ID = "record_id";

    List<FieldViewModel> fields;

    public static DetailActionFragment newInstance(long receiptId, long recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);
        args.putLong(ARG_RECORD_ID, recordId);

        DetailActionFragment fragment = new DetailActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActionPresenter)getPresenter()).loadFields(getArguments().getLong(ARG_RECORD_ID), getArguments().getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void onFormSuccess(FormViewModel form) {
        super.onFormSuccess(form);
        fields = form.getFields();
        container.setCardBackgroundColor(Color.WHITE);
        showFieldsInfo();
    }

    @Override
    public void fillFields(List<FieldViewModel> fields, boolean isEditMode) {
        if(formContainer.getChildCount() > 0)
            return;

        // saco esta linea para poder fillearlos aca mismo para cambiar la vista de los editable=false
//        super.fillFields(fields, isEditMode);
        if(formContainer.getChildCount() > 1)
            formContainer.removeAllViewsInLayout();

        for (int index = 0; index < fields.size(); index++){

            FieldViewModel field = fields.get(index);

            if (!field.isEditable() && field.isVisible()) {
                TextView text = new TextView(getContext());
                text.setTextColor(Color.BLACK);
                text.setTypeface(null, Typeface.BOLD);
                text.setBackgroundColor(Color.LTGRAY);

                text.setText(field.getLabel() + ": " + field.getValue());
                text.setVisibility(View.VISIBLE);

                formContainer.addView(text);
            } else {

                field.setValue("");  // se supone que si veo aca el formulario, estan vacios los datos! **********

                FieldViewFactory factory = field.getType().getFieldFactory();
                View view = factory.createView(formContainer, field, isEditMode);

                if (view != null) {
                    formValues.put(field.getTag(), field.getValue());
                    view.setTag(field.getTag());

                    FieldViewAdapter adapter = (FieldViewAdapter) view;
                    adapter.setFieldViewActionListener(this);
                    formContainer.addView(view);
                }
            }
        }
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new DetailActionLoader(getActivity());
    }

    @Override
    public void onSaveButtonClick() {
        Map<String, String> values = getFormValues();

        if (validateMandatory(values)) return;

        getActivity().setResult(Activity.RESULT_OK, createIntentData(values));

        // agrego un elemento al diccionario con el valor del location
        values.put(mapTag, FieldType.MAP.format(getLastLocation()));

        SaveFormTask.FormData data =
                new SaveFormTask.FormData(values,
                        getArguments().getLong(ARG_RECORD_ID),
                        getLastLocation());

        save(data);
    }

    private boolean validateMandatory(Map<String, String> values) {

        for (FieldViewModel field : fields) {
            if (field.isMandatory()) {
                if (values.get(field.getTag()) == null || values.get(field.getTag()).isEmpty() ||
                        values.get(field.getTag()).equals(SELECT_OPTION)) {
                    ((DetailActionActivity) getActivity()).showDialog("Atención", "El campo '" + field.getLabel() + "' es obligatorio");
                    return true;
                }
            }
        }

        if (values.get("OM_MOVILNOVEDAD_C_0049").equalsIgnoreCase("Z4")) {
            if (values.get("OM_MovilNovedad_cf_0400") == null) {
                ((DetailActionActivity) getActivity()).showDialog("Atención", "La firma es obligatoria");
                return true;
            }
        }

        return false;
    }

    private Intent createIntentData(Map<String, String> values) {
        Intent i = new Intent();
        Bundle extras = new Bundle();
        extras.putLong(EditReceiptFragment.RECORD_DATA, getArguments().getLong(ARG_RECORD_ID));
        for (String tag : values.keySet())
            for (FieldViewModel field : fields)
                if (field.getTag().equals(tag))
                    extras.putString(field.getLabel(), values.get(tag));

        i.putExtras(extras);
        return i;
    }

}