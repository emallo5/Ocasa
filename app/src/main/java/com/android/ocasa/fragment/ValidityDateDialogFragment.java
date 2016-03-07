package com.android.ocasa.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.Filter;
import com.android.ocasa.util.TimePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ignacio on 25/02/16.
 */
public class ValidityDateDialogFragment extends DialogFragment implements DatePickerDialogFragment.OnDateChangeListener,
        TimePickerDialogFragment.OnTimeChangeListener{

    private TextView validityDate;
    private TextView validityTime;

    private Calendar date;
    private Calendar time;

    private SendFormListener callback;

    public interface SendFormListener{
        public void send(Calendar validityDate);
    }

    public static ValidityDateDialogFragment newInstance() {

        Bundle args = new Bundle();

        ValidityDateDialogFragment fragment = new ValidityDateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (SendFormListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " mus implements SendFormListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getDelegate().getSupportActionBar();

        actionBar.setTitle("Actualizar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(android.);

        date = Calendar.getInstance();
        time = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_validity_date, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validityDate = (TextView) view.findViewById(R.id.date);
        validityDate.setText(DateTimeHelper.formatDate(date.getTime()));

        validityTime = (TextView) view.findViewById(R.id.time);
        validityTime.setText(DateTimeHelper.formatTime(time.getTime()));

        setListeners();
    }

    private void setListeners(){

        validityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment.newInstance(date, Calendar.getInstance().getTimeInMillis())
                        .show(getChildFragmentManager(), "DatePicker");
            }
        });

        validityTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialogFragment.newInstance("", time)
                        .show(getChildFragmentManager(), "TimePicker");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_form_send, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();

                return true;
            case R.id.send:

                Calendar validityDate = Calendar.getInstance();
                validityDate.set(date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH),
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE));

                callback.send(validityDate);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateChange(String fieldTag, Calendar calendar) {

        date = calendar;

        validityDate.setText(DateTimeHelper.formatDate(calendar.getTime()));
    }

    @Override
    public void onTimeChange(String fieldTag, Calendar calendar) {

        time = calendar;

        validityTime.setText(DateTimeHelper.formatTime(calendar.getTime()));
    }
}
