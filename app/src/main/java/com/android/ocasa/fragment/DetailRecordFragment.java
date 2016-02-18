package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.activity.ReadFieldActvivity;
import com.android.ocasa.barcode.BarcodeActivity;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.loader.RecordTaskLoader;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.TimePickerDialogFragment;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldDateView;
import com.android.ocasa.widget.FieldMapView;
import com.android.ocasa.widget.FieldPhoneView;
import com.android.ocasa.widget.FieldTimeView;
import com.android.ocasa.widget.FieldViewActionListener;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class DetailRecordFragment extends LocationFragment implements LoaderManager.LoaderCallbacks<Record>,
        FieldViewActionListener, TimePickerDialogFragment.OnTimeChangeListener,
        DatePickerDialogFragment.OnDateChangeListener{

    static final String ARG_RECORD_ID = "record_id";

    static final int REQUEST_QR_SCANNER = 1000;
    static final int REQUEST_MAP = 2000;

    private LinearLayout container;

    private TextView tableTitle;

    private Record record;

    public static DetailRecordFragment newInstance(int recordId) {

        Bundle args = new Bundle();
        args.putInt(ARG_RECORD_ID, recordId);

        DetailRecordFragment fragment = new DetailRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = (LinearLayout) view.findViewById(R.id.detail_container);
        tableTitle = (TextView) view.findViewById(R.id.table_title);
    }

    @Override
    public Loader<Record> onCreateLoader(int id, Bundle args) {
        return new RecordTaskLoader(getActivity(), args.getInt(ARG_RECORD_ID));
    }

    @Override
    public void onLoadFinished(Loader<Record> loader, Record data) {

        if(data != null){
            record = data;
            fillRecord();
        }
    }

    @Override
    public void onLoaderReset(Loader<Record> loader) {

    }

    private void fillRecord(){

        if(record.getTable().getName() != null)
            tableTitle.setText(record.getTable().getName().toUpperCase());

        fillFields(new ArrayList<>(record.getFields()));
    }

    private void fillFields(List<Field> fields){

        if(container.getChildCount() > 1)
            container.removeViews(1, fields.size() + 1);

        for (Field field : fields){
            FieldViewFactory factory = field.getColumn().getFieldType().getFieldFactory();

            View view = factory.createView(container, field);

            if(view != null) {
                try {
                    view.setTag(String.valueOf(field.getId()));

                    FieldViewAdapter adapter = (FieldViewAdapter) view;
                    adapter.setFieldViewActionListener(this);
                } catch (ClassCastException e) {

                }

                container.addView(view);
            }
        }

        View button = LayoutInflater.from(getActivity()).inflate(R.layout.send_button, container, false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        container.addView(button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_QR_SCANNER){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data == null)
                    return;

                Barcode barcode = data.getParcelableExtra(BarcodeActivity.BarcodeObject);

                FieldViewAdapter view = (FieldViewAdapter) container.findViewWithTag(data.getStringExtra(ReadFieldActvivity.QR_FIELD_TAG));

                view.setValue(barcode.displayValue);
            }
        }else if(requestCode == REQUEST_MAP){
            if(resultCode == Activity.RESULT_OK){

                LatLng location = data.getParcelableExtra(MapFragment.DATA_NEW_LOCATION);

                String columnId = data.getStringExtra(MapFragment.MAP_FIELD_TAG);

                FieldMapView view = (FieldMapView) container.findViewWithTag(columnId);
                view.setValue(FieldType.MAP.format(location));
            }
        }

    }


    @Override
    public void onHistoryClick(int fieldId) {
        FieldHistoricalFragmentRecycler fragment = FieldHistoricalFragmentRecycler.newInstance(fieldId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.android.ocasa.core.R.anim.slide_in_left,
                com.android.ocasa.core.R.anim.slide_out_left,
                com.android.ocasa.core.R.anim.slide_in_right,
                com.android.ocasa.core.R.anim.slide_out_right);
        transaction.hide(getFragmentManager().findFragmentByTag("Detail"));
        transaction.add(com.android.ocasa.core.R.id.container, fragment, "Historical");
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onQrClick(int fieldId) {

        Intent intent =  new Intent(getActivity(), ReadFieldActvivity.class);
        intent.putExtra(ReadFieldActvivity.QR_FIELD_TAG, String.valueOf(fieldId));

        startActivityForResult(intent, REQUEST_QR_SCANNER);
    }

    @Override
    public void onMapClick(FieldMapView view) {

        MapFragment fragment = MapFragment.newInstance((String) view.getTag(),
                (LatLng) FieldType.MAP.parse(view.getValue()));
        fragment.setTargetFragment(this, REQUEST_MAP);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.android.ocasa.core.R.anim.slide_in_left,
                com.android.ocasa.core.R.anim.slide_out_left,
                com.android.ocasa.core.R.anim.slide_in_right,
                com.android.ocasa.core.R.anim.slide_out_right);
        transaction.hide(getFragmentManager().findFragmentByTag("Detail"));
        transaction.add(com.android.ocasa.core.R.id.container, fragment, "Map");
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onDateClick(FieldDateView view) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTimeHelper.parseDate(view.getValue()));

        DatePickerDialogFragment datePickerDialog = DatePickerDialogFragment.newInstance((String) view.getTag(), calendar);
        datePickerDialog.show(getChildFragmentManager(), "DateDialog");
    }

    @Override
    public void onTimeClick(FieldTimeView view) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTimeHelper.parseTime(view.getValue()));

        TimePickerDialogFragment timePickerDialog = TimePickerDialogFragment.newInstance((String) view.getTag(), calendar);
        timePickerDialog.show(getChildFragmentManager(), "TimeDialog");
    }

    @Override
    public void onPhoneClick(FieldPhoneView view) {
        Intent call = new Intent(Intent.ACTION_CALL,  Uri.parse("tel:" + view.getValue()));
        startActivity(call);
    }

    @Override
    public void onComboClick(FieldComboView view) {

        Table table = record.getColumnForField(Integer.valueOf(view.getTag().toString())).getRelationship();

        ComboTableFragment fragment = ComboTableFragment.newInstance(table.getId());

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(com.android.ocasa.core.R.anim.slide_in_left,
                com.android.ocasa.core.R.anim.slide_out_left,
                com.android.ocasa.core.R.anim.slide_in_right,
                com.android.ocasa.core.R.anim.slide_out_right);
        transaction.hide(getFragmentManager().findFragmentByTag("Detail"));
        transaction.add(com.android.ocasa.core.R.id.container, fragment, "List");
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onTimeChange(String fieldTag, Calendar calendar) {
        FieldViewAdapter view = (FieldViewAdapter) container.findViewWithTag(fieldTag);

        view.setValue(DateTimeHelper.formatTime(calendar.getTime()));
    }

    @Override
    public void onDateChange(String fieldTag, Calendar calendar) {
        FieldViewAdapter view = (FieldViewAdapter) container.findViewWithTag(fieldTag);

        view.setValue(DateTimeHelper.formatDate(calendar.getTime()));
    }

    private void save(){

        for (Field field : record.getFields()){

            FieldViewAdapter view = (FieldViewAdapter) container.findViewWithTag(String.valueOf(field.getId()));

            if(!view.getValue().equalsIgnoreCase(field.getValue())) {

                History history = new History();
                history.setValue(field.getValue());
                history.setDate(DateTimeHelper.formatDateTime(new Date()));
                history.setField(field);

                field.addHistory(history);

                field.setValue(view.getValue());
            }
        }

        new SaveFormTask(getActivity()).execute(record);

        Toast.makeText(getContext(), "Enviando...", Toast.LENGTH_SHORT).show();

        getActivity().onBackPressed();
    }

}
