package com.android.ocasa.receipt.item.detailaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.Constants;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.FileHelper;
import com.android.ocasa.util.KeyboardUtil;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leandro on 3/5/17.
 */

public class MasiveDetailActionFragment extends FormFragment {

    static String ARG_RECEIPT_ID = "receipt_id";
    static String ARG_RECORD_ID = "record_id";

    public static final String EXIT_POD = "exit";
    public static final int REQUEST_WRITE_STORAGE = 200;

    private boolean readyToSave = false;
    private View name;
    private View signature;

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

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            return false;
        }
        return true;
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

        if(formContainer.getChildCount() > 1)
            formContainer.removeAllViewsInLayout();

        for (int index = 0; index < fields.size(); index++) {

            FieldViewModel field = fields.get(index);

            // OM_MOVILNOVEDAD_C_0049 direccion
            // OM_MOVILNOVEDAD_C_0014 tipoServicio
            // OM_MOVILNOVEDAD_C_0050 nombre
            // OM_MovilNovedad_cf_0400 firma

            if (!field.isEditable() && field.isVisible()) {
                TextView text = new TextView(getContext());
                text.setTextColor(Color.BLACK);
                text.setTypeface(null, Typeface.BOLD);
                text.setBackgroundColor(Color.LTGRAY);

                text.setText(field.getLabel() + ": " + field.getValue());
                text.setVisibility(View.GONE);

                if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0049") || field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0014"))
                    text.setVisibility(View.VISIBLE);

                formContainer.addView(text);
            } else {
                if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0050") || field.getTag().equalsIgnoreCase("OM_MovilNovedad_cf_0400")) {
                    field.setValue("");
                    FieldViewFactory factory = field.getType().getFieldFactory();
                    View view = factory.createView(formContainer, field, isEditMode);

                    if (view != null) {
                        formValues.put(field.getTag(), field.getValue());
                        view.setTag(field.getTag());

                        FieldViewAdapter adapter = (FieldViewAdapter) view;
                        adapter.setFieldViewActionListener(this);

                        view.setVisibility(View.GONE);
                        if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0050")) name = view;
                        if (field.getTag().equalsIgnoreCase("OM_MovilNovedad_cf_0400")) signature = view;
                        formContainer.addView(view);
                    }
                }
            }
        }

//        formContainer.findViewWithTag();
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new DetailActionLoader(getActivity());
    }

    @Override
    public void onSaveButtonClick() {
        KeyboardUtil.hideKeyboard(getActivity());
        if (readyToSave) {
            saveAndExit(true);
        } else {
            readyToSave = true;
            name.setVisibility(View.VISIBLE);
            signature.setVisibility(View.VISIBLE);
        }
    }

    public void saveAndExit(boolean exit) {

        if (!checkPermission()) {
            Toast.makeText(getContext(), "DEBE HABILITAR LOS PERMISOS!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> values = getFormValues();

        if (validateMandatory(values)) return;

        showProgress();
        getActivity().setResult(Activity.RESULT_OK, createIntentData(values, exit));

        // agrego los elementos de map y hora. Fueron sacados en el onFormSucces() del padre
        values.put(timeTag, DateTimeHelper.formatTime(new Date()));
        values.put(mapTag, FieldType.MAP.format(requestLoction()));

        SaveFormTask.FormData data = new SaveFormTask.FormData(values, getArguments().getLong(ARG_RECORD_ID), getLastLocation());

        save(data);

        FileHelper.getInstance().saveLocation(getLastLocation().getLatitude() + " "
                + getLastLocation().getLongitude() + " " + FileHelper.IS_POD + " " + getIdSistema());
    }

    private String getIdSistema() {
        for (FieldViewModel field : fields)
            if (field.getTag().equals("OM_MOVILNOVEDAD_C_0038"))
                return field.getValue();
        return "Sin ID";
    }

    private boolean validateMandatory(Map<String, String> values) {

        if (values.get("OM_MovilNovedad_cf_0400") == null) {
            ((DetailActionActivity) getActivity()).showDialog("Atención", "La firma es obligatoria");
            return true;
        }

        if (values.get("OM_MovilNovedad_cf_0400") == null) { // TODO: buscar codigo del receptor
            ((DetailActionActivity) getActivity()).showDialog("Atención", "Debe completar el nombre del receptor");
            return true;
        }

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

    private Location requestLoction() {

        Location location = getLastLocation();

        try {
            String[] loc = ConfigHelper.getInstance().ReadConfig(Constants.LAST_LOCATION).split("-");
            Location locationSaved = new Location("Dummy");
            locationSaved.setLatitude(Double.valueOf(loc[0]));
            locationSaved.setLongitude(Double.valueOf(loc[1]));
            location = locationSaved;
        } catch (Exception e) {}

        return location;
    }

    public void showProgress() {
        ProgressDialogFragment pdf = ProgressDialogFragment.newInstance("Guardando...");
        pdf.setCancelable(false);
        pdf.show(getChildFragmentManager(), "Progress");
    }

}
