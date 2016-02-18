package com.android.ocasa.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ignacio on 01/02/16.
 */
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    static final String TIME_FIELD_TAG = "field_tag";
    static final String ARG_HOUR = "hour";
    static final String ARG_MINUTE = "minute";

    private OnTimeChangeListener callback;

    public interface OnTimeChangeListener{
        public void onTimeChange(String fieldTag, Calendar calendar);
    }

    public static TimePickerDialogFragment newInstance(String fieldTag, Calendar calendar) {
        
        Bundle args = new Bundle();
        args.putString(TIME_FIELD_TAG, fieldTag);
        args.putInt(ARG_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
        args.putInt(ARG_MINUTE, calendar.get(Calendar.MINUTE));
        
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnTimeChangeListener) getParentFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("must implement OnTimeChangeListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = getArguments().getInt(ARG_HOUR);
        int minute = getArguments().getInt(ARG_MINUTE);

        return new TimePickerDialog(getActivity(), com.android.ocasa.core.R.style.SlideDialog, this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        String fieldTag = getArguments().getString(TIME_FIELD_TAG);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i1);

        callback.onTimeChange(fieldTag, calendar);
    }
}
