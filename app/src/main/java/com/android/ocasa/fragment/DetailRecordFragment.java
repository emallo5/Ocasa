package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.activity.ReadFieldActvivity;
import com.android.ocasa.activity.ValidityDateActivity;
import com.android.ocasa.barcode.BarcodeActivity;
import com.android.ocasa.loader.RecordTaskLoader;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.TimePickerDialogFragment;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldDateView;
import com.android.ocasa.widget.FieldListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ignacio on 28/01/16.
 */
public class DetailRecordFragment extends FormRecordFragment implements
        FieldViewActionListener, TimePickerDialogFragment.OnTimeChangeListener,
        DatePickerDialogFragment.OnDateChangeListener, ComboPickerDialog.OnComboPickerListener{

    public static DetailRecordFragment newInstance(long recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId);

        DetailRecordFragment fragment = new DetailRecordFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    String title = getString(R.string.detail_title, getRecord().getTable().getName());

                    setTitle(title);
                }
            }
        });
    }

    @Override
    public void fillRecord(Record record) {
        if(record.getTable().getName() != null){

            String title = getString(R.string.detail_title, record.getTable().getName());

            setTitle(title);
        }

        fillFields(new ArrayList<>(record.getFields()));
    }

    @Override
    public void onSaveButtonClick() {
        Map<String, String> formValues = getFormValues();

        SaveFormTask.FormData formData = new SaveFormTask.FormData(formValues, getArguments().getLong(ARG_RECORD_ID), getLastLocation());
        save(formData);
    }
}
