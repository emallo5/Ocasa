package com.android.ocasa.receipt.item.detailaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.PopupMenu;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.PodStructuresById;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.Constants;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.ExpandedTextFragment;
import com.android.ocasa.util.FileHelper;
import com.android.ocasa.util.KeyboardUtil;
import com.android.ocasa.util.Operator;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.util.SignatureDialogFragment;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.TextFieldView;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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

//        super.fillFields(fields, isEditMode);   hago el fill a mano para cambiar los editable=false
        if(formContainer.getChildCount() > 1)
            formContainer.removeAllViewsInLayout();

        for (int index = 0; index < fields.size(); index++) {

            final FieldViewModel field = fields.get(index);

            if (!field.isEditable() && field.isVisible()) {

                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                TextView tvLabel = new TextView(getContext());
                tvLabel.setTextColor(Color.BLACK);
                tvLabel.setTypeface(null, Typeface.BOLD);
                tvLabel.setBackgroundColor(Color.LTGRAY);
                tvLabel.setText(field.getLabel() + ": ");

                TextView text = new TextView(getContext());
                text.setTextColor(Color.BLACK);
                text.setBackgroundColor(Color.LTGRAY);
                final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                text.setLayoutParams(lp);

                text.setEllipsize(TextUtils.TruncateAt.END);
                text.setSingleLine(true);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView = (TextView)v;
                        Layout l = textView.getLayout();
                        if (l != null) {
                            int lines = l.getLineCount();
                            if (lines > 0)
                                if (l.getEllipsisCount(lines-1) > 0)
                                    ExpandedTextFragment.newInstance(field.getLabel(), field.getValue())
                                            .show(getChildFragmentManager(), "ExpandedText");
                        }
                    }
                });

                if (field.getType() == FieldType.TIME) {
                    field.setValue(DateTimeHelper.formatTime(new Date()));
                    timePodTag = field.getTag() + "-" + field.getValue();
                    ll.setVisibility(View.GONE);
                }

                text.setText(field.getValue());

                if (field.getValue().isEmpty()) ll.setVisibility(View.GONE);

                ll.addView(tvLabel);
                ll.addView(text);

                formContainer.addView(ll);
            } else {

                field.setValue("");  // vacio los datos que pueden haber quedado sucios
                FieldViewFactory factory = field.getType().getFieldFactory();
                View view = factory.createView(formContainer, field, isEditMode);

                if (view != null) {
                    formValues.put(field.getTag(), field.getValue());
                    view.setTag(field.getTag());

                    FieldViewAdapter adapter = (FieldViewAdapter) view;
                    adapter.setFieldViewActionListener(this);
                    if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0072")) view.setVisibility(View.GONE);
                    formContainer.addView(view);

                    view.setVisibility(podStructure.containsColumn(field.getTag()) ? View.VISIBLE : View.GONE);
                }
            }

            // valor por defecto de MOTIVO a partir del Tipo de Servicio
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

        if (!checkPermission()) {
            Toast.makeText(getContext(), "DEBE HABILITAR LOS PERMISOS!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> values = getFormValues();

        if (validateMandatory(values)) return;

        showProgress();
        getActivity().setResult(Activity.RESULT_OK, createIntentData(values, exit));

        // agrego map. Fue sacado en el onFormSucces() del padre y la hora POD que no es editable pero hay que setearla
        if (!timePodTag.isEmpty()) values.put(timePodTag.split("-")[0], timePodTag.split("-")[1]);
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

        if (podStructure != null) {
            for (PodStructuresById.VisibleColumn column : podStructure.getColumns()) {
                if (column.isMandatory()) {
                    if (values.get(column.getId()) == null || values.get(column.getId()).isEmpty() || values.get(column.getId()).equals(SELECT_OPTION)) {
                        ((DetailActionActivity) getActivity()).showDialog("Atención", "El campo '" + column.getName() + "' es obligatorio");
                        return true;
                    }
                } else {
                    if (column.getRules() == null || column.getRules().isEmpty()) continue;
                    for (PodStructuresById.Rule rule : column.getRules()) {
                        if (Operator.findOperator(rule.getOperator()).operate(values.get(rule.getId()), rule.getValue())) {
                            if (values.get(column.getId()) == null || values.get(column.getId()).isEmpty() ||
                                    values.get(column.getId()).equals(SELECT_OPTION)) {
                                ((DetailActionActivity) getActivity()).showDialog("Atención", "El campo '" + column.getName() + "' es obligatorio");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        else {
            for (FieldViewModel field : fields) {
                if (field.isMandatory()) {
                    if (values.get(field.getTag()) == null || values.get(field.getTag()).isEmpty() ||
                            values.get(field.getTag()).equals(SELECT_OPTION)) {
                        ((DetailActionActivity) getActivity()).showDialog("Atención", "El campo '" + field.getLabel() + "' es obligatorio");
                        return true;
                    }
                }
            }

            if (!values.get("OM_MOVILNOVEDAD_C_0049").equalsIgnoreCase("Z4") && !values.get("OM_MOVILNOVEDAD_C_0049").equalsIgnoreCase("Z1")) {
                if (values.get("OM_MOVILNOVEDAD_CF_0500") == null && values.get("OM_MOVILNOVEDAD_CF_0600") == null
                        && values.get("OM_MOVILNOVEDAD_CF_0700") == null && values.get("OM_MOVILNOVEDAD_CF_0800") == null) {
                    ((DetailActionActivity) getActivity()).showDialog("Atención", "Se requiere al menos una foto");
                    return true;
                }
            } else {
                if (values.get("OM_MovilNovedad_cf_0400") == null) {
                    ((DetailActionActivity) getActivity()).showDialog("Atención", "La firma es obligatoria");
                    return true;
                }
            }
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

    public void hideProgress() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if(dialog != null) dialog.dismiss();
    }
}