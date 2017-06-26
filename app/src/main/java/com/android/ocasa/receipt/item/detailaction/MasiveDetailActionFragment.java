package com.android.ocasa.receipt.item.detailaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.media.MediaPlayer;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.activity.ReadFieldActvivity;
import com.android.ocasa.barcode.BarcodeActivity;
import com.android.ocasa.cache.dao.NewRecordReadDAO;
import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.NewRecordRead;
import com.android.ocasa.pickup.scanner.ScannerActivity;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.util.AlertDialogFragment;
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
import com.android.ocasa.widget.TagReaderView;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leandro on 3/5/17.
 */

public class MasiveDetailActionFragment extends FormFragment implements TagReaderView.OnActionClickListener {

    static String ARG_RECEIPT_ID = "receipt_id";
    static String ARG_RECORD_ID = "record_id";

    public static final String EXIT_POD = "exit";
    public static final int REQUEST_WRITE_STORAGE = 200;
    public static final int REQUEST_CODE_READ = 300;

    private boolean readyToSave = false;
    private View name;
    private View recibo;
    private View signature;

    private MediaPlayer errorSound;
    private MediaPlayer checkSound;

    List<FieldViewModel> fields;
    ArrayList<String> readCodes = new ArrayList<>();
    TagReaderView tagReaderView;

    public static MasiveDetailActionFragment newInstance(long receiptId, long recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);
        args.putLong(ARG_RECORD_ID, recordId);

        MasiveDetailActionFragment fragment = new MasiveDetailActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AVAILABLE_GPS_FUNCTION = true;
        super.onCreate(savedInstanceState);

        checkSound = MediaPlayer.create(getActivity(), R.raw.check_in_sound);
        errorSound = MediaPlayer.create(getActivity(), R.raw.error_sound);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActionPresenter)getPresenter()).loadFields(getArguments().getLong(ARG_RECORD_ID), getArguments().getLong(ARG_RECEIPT_ID));

        checkPermission();
    }

    @Override
    public void onMenuCreated() {
        super.onStart();
        changeSendButtonText(readyToSave ? "ENVIAR" : "FINALIZAR");
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

            // OM_MOVILNOVEDAD_C_0043 direccion
            // OM_MOVILNOVEDAD_C_0014 tipoServicio
            // OM_MOVILNOVEDAD_C_0050 nombre
            // OM_MovilNovedad_cf_0400 firma
            // OM_MOVILNOVEDAD_C_0072 recibo

            if (!field.isEditable() && field.isVisible()) {
                TextView text = new TextView(getContext());
                text.setTextColor(Color.BLACK);
                text.setTypeface(null, Typeface.BOLD);
                text.setBackgroundColor(Color.LTGRAY);

                text.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                text.setSingleLine(true);
                text.setHorizontallyScrolling(true);
                text.setMarqueeRepeatLimit(3);
                text.setFocusable(true);
                text.setFocusableInTouchMode(true);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setSelected(true);
                    }
                });

                if (field.getType() == FieldType.TIME) {
                    field.setValue(DateTimeHelper.formatTime(new Date()));
                    timePodTag = field.getTag() + "-" + field.getValue();
                }

                text.setText(field.getLabel() + ": " + field.getValue());
                text.setVisibility(View.GONE);

                if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0043") || field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0014"))
                    text.setVisibility(View.VISIBLE);

                formContainer.addView(text);
            } else {

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
                    if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0072")) recibo = view;
                    if (field.getTag().equalsIgnoreCase("OM_MovilNovedad_cf_0400")) signature = view;

                    formContainer.addView(view);
                }
            }
        }

        tagReaderView = new TagReaderView(getContext());
        tagReaderView.setOnActionClickListener(this);
        formContainer.addView(tagReaderView);
        tagReaderView.setCountRead(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_READ) {
            if(resultCode != Activity.RESULT_OK)
                return;

            if(data == null) return;
            addCode(data.getStringExtra(ScannerActivity.EXTRA_RESULT_CODES));
        }
    }

    @Override
    public void onReadTagClick() {
        startActivityForResult(new Intent(getActivity(), ScannerActivity.class), REQUEST_CODE_READ);
    }

    @Override
    public void onEnterKeyPressed(String code) {
        addCode(code);
    }

    public void addCode(String code) {

        if (readCodes.contains(code)) {
            errorSound.start();
            ((DetailActionActivity) getActivity()).showDialog("Atención", "Este código ya existe!");
            return;
        }

        checkSound.start();
        TextView textView = new TextView(getContext());
        textView.setText(code);
        textView.setTextSize(16);
        formContainer.addView(textView);

        readCodes.add(code);
        tagReaderView.setCodeRead("");
        tagReaderView.setCountRead(readCodes.size());
        KeyboardUtil.hideKeyboard(getActivity());
    }

    private void saveReadCodes() {
        NewRecordReadDAO newRecordReadDAO = new NewRecordReadDAO(getContext());
        for (String code : readCodes) {
            NewRecordRead rec  = new NewRecordRead();
            rec.setRead(code);
            rec.setRecordId(String.valueOf(getArguments().getLong(ARG_RECORD_ID)));
            newRecordReadDAO.save(rec);
        }
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
            changeSendButtonText("ENVIAR");
            name.setVisibility(View.VISIBLE);
            if (recibo != null) recibo.setVisibility(View.VISIBLE);
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

        // agrego map. Fue sacado en el onFormSucces() del padre
        if (!timePodTag.isEmpty()) values.put(timePodTag.split("-")[0], timePodTag.split("-")[1]);
        values.put(mapTag, FieldType.MAP.format(requestLoction()));

        SaveFormTask.FormData data = new SaveFormTask.FormData(values, getArguments().getLong(ARG_RECORD_ID), getLastLocation());

        saveReadCodes();
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

        if (readCodes.size() == 0) {
            ((DetailActionActivity) getActivity()).showDialog("Atención", "No agregó ningún elemento a la lista!");
            return true;
        }

//        if (values.get("OM_MovilNovedad_cf_0400") == null) {
//            ((DetailActionActivity) getActivity()).showDialog("Atención", "La firma es obligatoria");
//            return true;
//        }

        if (values.get("OM_MOVILNOVEDAD_C_0050").isEmpty()) {
            ((DetailActionActivity) getActivity()).showDialog("Atención", "Debe completar el nombre del receptor");
            return true;
        }

        if (values.get("OM_MOVILNOVEDAD_C_0072").isEmpty()) {
            ((DetailActionActivity) getActivity()).showDialog("Atención", "Debe completar el campo Recibo imposición");
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
