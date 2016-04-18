package com.android.ocasa.fragment;

import android.nfc.FormatException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.loader.ActionTaskLoaderTest;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.util.TimePickerDialogFragment;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.FieldComboView;
import com.android.ocasa.widget.FieldDateView;
import com.android.ocasa.widget.FieldListView;
import com.android.ocasa.widget.FieldMapView;
import com.android.ocasa.widget.FieldPhoneView;
import com.android.ocasa.widget.FieldTimeView;
import com.android.ocasa.widget.FieldViewActionListener;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Ignacio Oviedo on 04/04/16.
 */
public class CreateHeaderReceiptFragment extends BaseActionFragment implements
        DatePickerDialogFragment.OnDateChangeListener,
        TimePickerDialogFragment.OnTimeChangeListener,
        FieldViewActionListener,
        ComboPickerDialog.OnComboPickerListener{

    private TextView dateView;
    private TextView timeView;

    private LinearLayout container;

    private Map<String, String> formValues;

    private Calendar date;
    private Calendar time;

    public static CreateHeaderReceiptFragment newInstance(String actionId) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        CreateHeaderReceiptFragment fragment = new CreateHeaderReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formValues = new HashMap<>();

        date = Calendar.getInstance();
        time = Calendar.getInstance();

        getLoaderManager().initLoader(1, getArguments(), new FormViewModelCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_receipt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateView = (TextView) view.findViewById(R.id.date);
        dateView.setText(DateTimeHelper.formatDate(date.getTime()));

        timeView = (TextView) view.findViewById(R.id.time);
        timeView.setText(DateTimeHelper.formatTime(time.getTime()));

        container = (LinearLayout) view.findViewById(R.id.container);

        setListeners();
    }

    private void setListeners(){
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateTimeHelper.parseDate(((TextView) view).getText().toString()));

                DatePickerDialogFragment datePickerDialog = DatePickerDialogFragment.newInstance((String) view.getTag(), calendar);
                showDialog("Date", datePickerDialog);
            }
        });

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateTimeHelper.parseTime(((TextView) view).getText().toString()));

                TimePickerDialogFragment timePickerDialog = TimePickerDialogFragment.newInstance((String) view.getTag(), calendar);
                showDialog("Time", timePickerDialog);
            }
        });
    }

    @Override
    public void onDateChange(String fieldTag, Calendar calendar) {
        dateView.setText(DateTimeHelper.formatDate(calendar.getTime()));
        date = calendar;
    }

    @Override
    public void onTimeChange(String fieldTag, Calendar calendar) {
        timeView.setText(DateTimeHelper.formatTime(calendar.getTime()));
        time = calendar;
    }

    public Map<String, String> getHeaderValues(){
        for (Map.Entry<String, String> pair : formValues.entrySet()) {
            FieldViewAdapter view = (FieldViewAdapter) container.findViewWithTag(pair.getKey());

            pair.setValue(view.getValue());
        }

        return formValues;
    }

    @Override
    public void onHistoryClick(View view) {

    }

    @Override
    public void onQrClick(View view) {

    }

    @Override
    public void onMapClick(FieldMapView view) {

    }

    @Override
    public void onDateClick(FieldDateView view) {

    }

    @Override
    public void onTimeClick(FieldTimeView view) {

    }

    @Override
    public void onPhoneClick(FieldPhoneView view) {

    }

    @Override
    public void onComboClick(FieldComboView view) {
        Table table = action.getHeaderColumnForId(view.getTag().toString()).getRelationship();

        ComboPickerDialog dialog = ComboPickerDialog.newInstance(view.getTag().toString(), table.getId());
        showDialog("Combo", dialog);
    }

    @Override
    public void onListClick(FieldListView view) {

    }

    @Override
    public void onPick(String fieldTag, CellViewModel record) {
        FieldComboView comboView = (FieldComboView) container.findViewWithTag(fieldTag);
        comboView.setValue(record.getValue());

        for (FieldViewModel field : record.getFields()){

            FieldViewAdapter adapter = (FieldViewAdapter) comboView.findViewWithTag(field.getTag());

            try {
                adapter.setValue(field.getValue());
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Action> loader, Action data) {
        super.onLoadFinished(loader, data);

        setTitle(data.getName());
    }

    private void fillFields(FormViewModel form){

        if(container.getChildCount() > 2)
            return;

        for (FieldViewModel field : form.getFields()){

            FieldViewFactory factory = field.getType().getFieldFactory();

            formValues.put(field.getTag(), "");

            View view = factory.createView(container, field, true);
            view.setTag(field.getTag());

            FieldViewAdapter adapter = (FieldViewAdapter) view;
            adapter.setFieldViewActionListener(this);

            container.addView(view);
        }
    }

    public Date getValidityDate(){
        Calendar validityDate = Calendar.getInstance();
        validityDate.set(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE));

        return validityDate.getTime();
    }

    private class FormViewModelCallback implements LoaderManager.LoaderCallbacks<FormViewModel>{

        @Override
        public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
            return new ActionTaskLoaderTest(getActivity(), args.getString(ARG_ACTION_ID));
        }

        @Override
        public void onLoadFinished(Loader<FormViewModel> loader, FormViewModel data) {
            fillFields(data);
        }

        @Override
        public void onLoaderReset(Loader<FormViewModel> loader) {}

    }
}

