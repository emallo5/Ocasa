package com.android.ocasa.record.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.android.ocasa.R;

import com.android.ocasa.core.FormFragment;
import com.android.ocasa.fragment.ComboPickerDialog;
import com.android.ocasa.loader.SaveFormTask;

import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.util.TimePickerDialogFragment;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldViewActionListener;

import java.util.Map;

/**
 * Ignacio Oviedo on 28/01/16.
 */
public class DetailRecordFragment extends FormFragment implements
        FieldViewActionListener, TimePickerDialogFragment.OnTimeChangeListener,
        DatePickerDialogFragment.OnDateChangeListener, ComboPickerDialog.OnComboPickerListener {

    static final String ARG_RECORD_ID = "record_id";


    public static DetailRecordFragment newInstance(long recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId);

        DetailRecordFragment fragment = new DetailRecordFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().load(getArguments().getLong(ARG_RECORD_ID));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    String title = getString(R.string.detail_title, getRecord().getTitle());

                    setTitle(title);
                }
            }
        });
    }

    @Override
    public void onFormSuccess(FormViewModel form) {
        super.onFormSuccess(form);

        container.setCardBackgroundColor(Color.parseColor(form.getColor()));

        String title = getString(R.string.detail_title, form.getTitle());
        setTitle(title);
    }

    @Override
    public void onSaveButtonClick() {
        Map<String, String> formValues = getFormValues();

        SaveFormTask.FormData formData = new SaveFormTask.FormData(formValues, getArguments().getLong(ARG_RECORD_ID), getLastLocation());
        save(formData);
    }
}
