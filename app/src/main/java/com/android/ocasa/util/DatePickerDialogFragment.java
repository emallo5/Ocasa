package com.android.ocasa.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ignacio on 01/02/16.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    static final String DATE_FIELD_TAG = "field_tag";
    static final String ARG_MIN_DATE = "min_date";
    static final String ARG_DAY = "day";
    static final String ARG_MONTH = "month";
    static final String ARG_YEAR = "year";

    private OnDateChangeListener callback;

    public interface OnDateChangeListener{
        public void onDateChange(String fieldTag, Calendar calendar);
    }

    public static DatePickerDialogFragment newInstance(String fieldTag, Calendar calendar) {
        
        Bundle args = new Bundle();
        args.putString(DATE_FIELD_TAG, fieldTag);
        args.putInt(ARG_DAY, calendar.get(Calendar.DAY_OF_MONTH));
        args.putInt(ARG_MONTH, calendar.get(Calendar.MONTH));
        args.putInt(ARG_YEAR, calendar.get(Calendar.YEAR));

        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DatePickerDialogFragment newInstance(Calendar calendar, long minDate) {

        Bundle args = new Bundle();
        args.putInt(ARG_DAY, calendar.get(Calendar.DAY_OF_MONTH));
        args.putInt(ARG_MONTH, calendar.get(Calendar.MONTH));
        args.putInt(ARG_YEAR, calendar.get(Calendar.YEAR));
        args.putLong(ARG_MIN_DATE, minDate);

        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnDateChangeListener) getParentFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("must implement OnTimeChangeListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = getArguments().getInt(ARG_YEAR);
        int month = getArguments().getInt(ARG_MONTH);
        int day = getArguments().getInt(ARG_DAY);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                com.android.ocasa.core.R.style.PickerDialog, this, year, month, day);

        if(getArguments().containsKey(ARG_MIN_DATE))
            dialog.getDatePicker().setMinDate(getArguments().getLong(ARG_MIN_DATE));

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        String fieldTag = getArguments().getString(DATE_FIELD_TAG);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        callback.onDateChange(fieldTag, calendar);
    }
}
