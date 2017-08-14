package com.android.ocasa.core;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.activity.ReadFieldActvivity;
import com.android.ocasa.barcode.BarcodeActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.fragment.ComboPickerDialog;
import com.android.ocasa.fragment.FieldHistoricalFragment;
import com.android.ocasa.fragment.MapFragment;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.PodStructuresById;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.FieldDetailDialogFragment;
import com.android.ocasa.util.MediaUtils;
import com.android.ocasa.util.SignatureDialogFragment;
import com.android.ocasa.util.TimePickerDialogFragment;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldDateTimeView;
import com.android.ocasa.widget.FieldDateView;
import com.android.ocasa.widget.FieldListView;
import com.android.ocasa.widget.FieldMapView;
import com.android.ocasa.widget.FieldPhoneView;
import com.android.ocasa.widget.FieldPhotoView;
import com.android.ocasa.widget.FieldSignatureView;
import com.android.ocasa.widget.FieldTimeView;
import com.android.ocasa.widget.FieldViewActionListener;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.SignatureView;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.io.FileSystem;

/**
 * Created by ignacio on 11/07/16.
 */
public abstract class FormFragment extends LocationMvpFragment<FormView, FormPresenter> implements FormView,
        FieldViewActionListener, TimePickerDialogFragment.OnTimeChangeListener,
        DatePickerDialogFragment.OnDateChangeListener, ComboPickerDialog.OnComboPickerListener,
        FieldDetailDialogFragment.OnFieldSaveListener {

    static final int REQUEST_QR_SCANNER = 1000;
    static final int REQUEST_MAP = 2000;
    static final int REQUEST_PHOTO = 3000;

    static final int RC_HANDLE_CAMERA_PERM = 2;

    static final String MAP_TAG = "Map";
    static final String DATE_TAG = "Date";
    static final String TIME_TAG = "Time";
    static final String COMBO_TAG = "Combo";

    public static final String PREFIX = "/ocasa_";
    public static final String SELECT_OPTION = "Seleccionar...";

    private int margin = 8;

    protected Map<String, String> formValues;

    private FormViewModel record;

    protected CardView container;
    protected TextView tvCompleteText;
    protected LinearLayout formContainer;

    private FloatingActionButton detail;
    protected Menu menu;

    private File imageTempFile;

    private String currentPhotoTag;

    protected String mapTag = "";
    protected String timePodTag = "";

    protected PodStructuresById podStructure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        formValues = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.android.ocasa.R.layout.fragment_detail_record, container, false);
    }

    protected int getMargin(Context context) {
        return MediaUtils.dpToPixel(margin, context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = (CardView) view.findViewById(R.id.detail_container);
        tvCompleteText = (TextView) view.findViewById(R.id.tv_complete_text);
        formContainer = (LinearLayout) view.findViewById(R.id.detail_form_container);
        detail = (FloatingActionButton) view.findViewById(com.android.ocasa.R.id.detail);

        setListeners();
    }

    private void setListeners(){
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFieldsInfo();
            }
        });
    }

    public void showFieldsInfo(){
        for (Map.Entry<String, String> pair : formValues.entrySet()) {
            FieldViewAdapter view = (FieldViewAdapter) container.findViewWithTag(pair.getKey());
            view.changeLabelVisbility(true);
        }
    }

    @Override
    public FormView getMvpView() {
        return this;
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new FormLoader(getActivity());
    }

    @Override
    public void onFormSuccess(FormViewModel form) {
        record = form;

        // saco el valor de MAP a mano porque es visible pero no tiene que mostrarse, lo agrego en el saveAndExit() del hijo
        for (int i=form.getFields().size() - 1; i>-1; i--) {
            if (form.getFields().get(i).getType() == FieldType.MAP) {
                mapTag = form.getFields().get(i).getTag();
                form.getFields().remove(i);
            }
//            else if (form.getFields().get(i).getType() == FieldType.TIME) {
//                timeTag = form.getFields().get(i).getTag();
//                form.getFields().remove(i);
//            }
        }

        setPodStructure(form.getFields());
        fillFields(form.getFields());
    }

    protected void setPodStructure(List<FieldViewModel> fields) {
        for (FieldViewModel field : fields) {
            if (field.getTag().equalsIgnoreCase("OM_MOVILNOVEDAD_C_0006")) {
                for (PodStructuresById structure : ConfigHelper.getInstance().getPodStructureByIdList()) {
                    if (structure.getId().equalsIgnoreCase(field.getValue())) {
                        this.podStructure = structure;
                        return;
                    } else if (structure.getId().equalsIgnoreCase("0"))
                        this.podStructure = structure;
                }
            }
        }
    }

    public void setRecordForm(FormViewModel form){
        this.record = form;
    }

    public void fillFields(List<FieldViewModel> fields){
        fillFields(fields, false);
    }

    public void fillFields(List<FieldViewModel> fields, boolean isEditMode){

        if(formContainer.getChildCount() > 1)
            formContainer.removeAllViewsInLayout();

        for (int index = 0; index < fields.size(); index++){

            FieldViewModel field = fields.get(index);

            FieldViewFactory factory = field.getType().getFieldFactory();

            View view = factory.createView(formContainer, field, isEditMode);

            if(view != null) {
                formValues.put(field.getTag(), field.getValue());
                view.setTag(field.getTag());

                FieldViewAdapter adapter = (FieldViewAdapter) view;
                adapter.setFieldViewActionListener(this);
                formContainer.addView(view);
            }
        }
    }

    protected void showCompleteText(String text) {
        tvCompleteText.setVisibility(tvCompleteText.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        tvCompleteText.setText(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_QR_SCANNER){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data == null)
                    return;

                Barcode barcode = data.getParcelableExtra(BarcodeActivity.BarcodeObject);

                FieldViewAdapter view = (FieldViewAdapter) formContainer.findViewWithTag(data.getStringExtra(ReadFieldActvivity.QR_FIELD_TAG));

                try {
                    view.setValue(barcode.displayValue);
                } catch (FormatException e) {
                    Toast.makeText(getActivity(), "Formato invalido", Toast.LENGTH_SHORT).show();
                }
            }
        } else if(requestCode == REQUEST_MAP) {
            if (resultCode == Activity.RESULT_OK) {

                LatLng location = data.getParcelableExtra(MapFragment.DATA_NEW_LOCATION);

                String columnId = data.getStringExtra(MapFragment.MAP_FIELD_TAG);

                FieldMapView view = (FieldMapView) formContainer.findViewWithTag(columnId);
                view.setValue(FieldType.MAP.format(location));
            }
        } else if(requestCode == REQUEST_PHOTO) {

            if (resultCode == Activity.RESULT_OK) {
//                try {
//                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
//                    OutputStream stream = new FileOutputStream(imageTempFile);
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//
//                    stream.flush();
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                if (imageTempFile.exists()) {
                    FieldViewAdapter view = (FieldViewAdapter) formContainer.findViewWithTag(currentPhotoTag);

                    try {
                        view.setValue(imageTempFile.getName());
                    } catch (FormatException e) {
                        e.printStackTrace();
                    }
                }
//                deleteLastFromDCIM(data.getExtras());
            }
        }
    }

    public void setTitle(String title){
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onHistoryClick(View view) {
        FieldHistoricalFragment fragment = FieldHistoricalFragment.newInstance(record.getId(),
                view.getTag().toString());

        showFragment("Detail", fragment, "Historical");
    }

    @Override
    public void onEditSignatureClick(FieldSignatureView view) {

        FieldViewModel field = record.getFieldForTag((String) view.getTag());

        SignatureDialogFragment.newInstance(field.getTag(), field.getLabel(), record.getId())
                .show(getChildFragmentManager(), "Signature");
    }

    @Override
    public void onEditPhotoClick(FieldPhotoView view) {

        currentPhotoTag = (String) view.getTag();

        final String[] permissions = new String[] { Manifest.permission.CAMERA };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

//        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                Manifest.permission.CAMERA)) {
//            requestPermissions(permissions, RC_HANDLE_CAMERA_PERM);
//            return;
//        }

        goTakePhoto();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_HANDLE_CAMERA_PERM) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goTakePhoto();
            }
        }
    }

    private void goTakePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                imageTempFile = createPhotoFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageTempFile));
                startActivityForResult(takePictureIntent, REQUEST_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File createPhotoFile() throws IOException {

        String imageFileName = PREFIX + new Date().getTime();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Ocasa";
        File storageDir = new File(path);

        if(!storageDir.exists())
            storageDir.mkdir();

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    @Override
    public void onQrClick(View view) {

        Intent intent =  new Intent(getActivity(), ReadFieldActvivity.class);
        intent.putExtra(ReadFieldActvivity.QR_FIELD_TAG, view.getTag().toString());

        startActivityForResult(intent, REQUEST_QR_SCANNER);
    }

    @Override
    public void onMapClick(FieldMapView view) {

        LatLng location = (LatLng) FieldType.MAP.parse(view.getValue());

        MapFragment fragment = MapFragment.newInstance((String) view.getTag(), view.getLabel(),
                location);
        fragment.setTargetFragment(this, REQUEST_MAP);

        showFragment("Detail", fragment, MAP_TAG);
    }

    @Override
    public void onDateClick(FieldDateView view) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTimeHelper.parseDate(view.getValue()));

        DatePickerDialogFragment datePickerDialog = DatePickerDialogFragment.newInstance((String) view.getTag(), calendar);
        showDialog(DATE_TAG, datePickerDialog);
    }

    @Override
    public void onTimeClick(FieldTimeView view) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTimeHelper.parseTime(view.getValue()));

        TimePickerDialogFragment timePickerDialog = TimePickerDialogFragment.newInstance((String) view.getTag(), calendar);
        showDialog(TIME_TAG, timePickerDialog);
    }

    @Override
    public void onPhoneClick(FieldPhoneView view) {
        Intent call = new Intent(Intent.ACTION_CALL,  Uri.parse("tel:" + view.getValue()));
        startActivity(call);
    }

    @Override
    public void onComboClick(FieldComboView view) {

        FieldViewModel field = record.getFieldForTag(view.getTag().toString());

        ComboPickerDialog dialog = ComboPickerDialog.newInstance(view.getTag().toString(), field.getRelationshipTable());
        showDialog(COMBO_TAG, dialog);
    }

    @Override
    public void onListClick(FieldListView view) {

        /*Column column = record.getFieldForColumn(view.getTag().toString()).getColumn();

        String value = record.getFieldForColumn(view.getValue()).getValue();

        DetailListFragment fragment = DetailListFragment.newInstance(view.getLabel(), column.getRelationship().getId(), "571", value);

        showFragment("Detail", fragment, "ListDetail");*/
    }

    @Override
    public void onPick(String fieldTag, CellViewModel record) {

        FieldComboView comboView = (FieldComboView) formContainer.findViewWithTag(fieldTag);
        comboView.setValue(record.getValue());

        for (FieldViewModel field : record.getFields()){

            FieldViewAdapter adapter = (FieldViewAdapter) comboView.findViewWithTag(field.getTag());
            try {
                adapter.setValue(field.getValue());
            } catch (FormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace(); //OM_MOVILNOVEDAD_C_0049 OM_MOTIVOENT_CLAVE OM_MOTIVOENT_CF_0200
                continue;
            }
        }
    }

    @Override
    public void onTimeChange(String fieldTag, Calendar calendar) {
        FieldViewAdapter view = (FieldViewAdapter) formContainer.findViewWithTag(fieldTag);

        if(view instanceof FieldDateTimeView){
            try {
                ((FieldDateTimeView) view).setTime(DateTimeHelper.formatTime(calendar.getTime()));
            } catch (FormatException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            view.setValue(DateTimeHelper.formatTime(calendar.getTime()));
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateChange(String fieldTag, Calendar calendar){
        FieldViewAdapter view = (FieldViewAdapter) formContainer.findViewWithTag(fieldTag);

        if(view instanceof FieldDateTimeView){
            try {
                ((FieldDateTimeView) view).setDate(DateTimeHelper.formatDate(calendar.getTime()));
            } catch (FormatException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            view.setValue(DateTimeHelper.formatDate(calendar.getTime()));
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSave(String tag, String value) {
        FieldViewAdapter view = (FieldViewAdapter) formContainer.findViewWithTag(tag);

        try {
            view.setValue(value);
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {

    }

    public FormViewModel getRecord(){
        return record;
    }

    public Map<String, String> getFormValues(){
        for (Map.Entry<String, String> pair : formValues.entrySet()) {
            FieldViewAdapter view = (FieldViewAdapter) formContainer.findViewWithTag(pair.getKey());

            pair.setValue(view.getValue());
        }

        return formValues;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(com.android.ocasa.R.menu.menu_form_send, menu);
        this.menu = menu;
        onMenuCreated();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.send:
                onSaveButtonClick();
                return true;
            case R.id.map:
                onMapButtonClick();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onMenuCreated() {
    }

    protected void changeSendButtonText(String title) {
        if (menu != null) menu.findItem(R.id.send).setTitle(title);
    }

    public abstract void onSaveButtonClick();

    public void onMapButtonClick() {
    }

    public void save(SaveFormTask.FormData formData) {
        new SaveFormTask(getActivity()).execute(formData);
    }
}
