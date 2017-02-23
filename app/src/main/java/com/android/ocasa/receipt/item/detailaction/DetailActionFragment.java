package com.android.ocasa.receipt.item.detailaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;

import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.KeyboardUtil;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.TextFieldView;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class DetailActionFragment extends FormFragment {

    static String ARG_RECEIPT_ID = "receipt_id";
    static String ARG_RECORD_ID = "record_id";

    public static final String EXIT_POD = "exit";
    public static final int REQUEST_WRITE_STORAGE = 200;

    private String motivoClave = "";
    private String motivoNombre = "";

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
    public void onCreate(Bundle savedInstanceState) {
        AVAILABLE_GPS_FUNCTION = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActionPresenter)getPresenter()).loadFields(getArguments().getLong(ARG_RECORD_ID), getArguments().getLong(ARG_RECEIPT_ID));

        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
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

//        super.fillFields(fields, isEditMode);   hago el fill a mano para cambiar los editable=false
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

                field.setValue("");  // vacio los datos que pueden haber quedado sucios
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

            // valor por defecto de MOTIVO
            if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0014")) {
                motivoClave = field.getValue().equals("E") ? "Z4" : "Z1";
                motivoNombre = field.getValue().equals("E") ? "ENTREGADO" : "RETIRADO";
            }
        }

        setDefaultMotivo();
    }

    private void setDefaultMotivo() {
        try {
            FieldComboView comboView = (FieldComboView) formContainer.findViewWithTag("OM_MOVILNOVEDAD_C_0049");
            comboView.setValue(motivoClave);
            ((FieldViewAdapter) comboView.findViewWithTag("OM_MOTIVOENT_CLAVE")).setValue(motivoClave);
            ((FieldViewAdapter) comboView.findViewWithTag("OM_MOTIVOENT_CF_0200")).setValue(motivoNombre);
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new DetailActionLoader(getActivity());
    }

    @Override
    public void onSaveButtonClick() {
        KeyboardUtil.hideKeyboard(getActivity());
        saveAndExit(true);
    }

    public void saveAndExit(boolean exit) {
        Map<String, String> values = getFormValues();

        if (validateMandatory(values)) return;

        showProgress();
        getActivity().setResult(Activity.RESULT_OK, createIntentData(values, exit));

        // agrego los elementos de map y hora. Fueron sacados en el onFormSucces() del padre
        values.put(timeTag, DateTimeHelper.formatTime(new Date()));
        values.put(mapTag, FieldType.MAP.format(getLastLocation()));

        SaveFormTask.FormData data = new SaveFormTask.FormData(values, getArguments().getLong(ARG_RECORD_ID), getLastLocation());

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

//        if (values.get("OM_MOVILNOVEDAD_C_0049").equalsIgnoreCase("Z4")) {
//            if (values.get("OM_MovilNovedad_cf_0400") == null) {
//                ((DetailActionActivity) getActivity()).showDialog("Atención", "La firma es obligatoria");
//                return true;
//            }
//        }

        return false;
    }

    private Intent createIntentData(Map<String, String> values, boolean exit) {
        Intent i = new Intent();
        Bundle extras = new Bundle();
        extras.putLong(EditReceiptFragment.RECORD_DATA, getArguments().getLong(ARG_RECORD_ID));
        for (String tag : values.keySet())
            for (FieldViewModel field : fields)
                if (field.getTag().equals(tag))
                    extras.putString(field.getLabel(), values.get(tag));

        extras.putBoolean(EXIT_POD, exit);
        i.putExtras(extras);
        return i;
    }

    public void showProgress() {
        ProgressDialogFragment pdf = ProgressDialogFragment.newInstance("Guardando...");
        pdf.setCancelable(false);
        pdf.show(getChildFragmentManager(), "Progress");
    }

    public void hideProgress() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if(dialog != null) dialog.dismiss();
    }
}