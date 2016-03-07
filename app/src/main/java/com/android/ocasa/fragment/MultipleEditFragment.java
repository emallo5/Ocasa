package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.ocasa.R;
import com.android.ocasa.activity.ValidityDateActivity;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.model.Record;
import com.android.ocasa.widget.FieldMapView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by ignacio on 07/03/16.
 */
public class MultipleEditFragment extends FormRecordFragment {

    static final String ARG_RECORDS_ID = "records_id";

    static final int REQUEST_VALIDITY_DATE = 3000;

    public static MultipleEditFragment newInstance(long[] recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId[0]);
        args.putLongArray(ARG_RECORDS_ID, recordId);

        MultipleEditFragment fragment = new MultipleEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fillRecord(Record record) {
        fillFields(new ArrayList<>(record.getFields()), true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_VALIDITY_DATE){
            if(resultCode == Activity.RESULT_OK){

                Calendar validityDate = (Calendar) data.getSerializableExtra("ValidityDate");

                save(validityDate);
            }
        }
    }

    @Override
    public void onMapClick(FieldMapView view) {

        MapFragment fragment = MapFragment.newInstance((String) view.getTag(), view.getLabel(),
                null);
        fragment.setTargetFragment(this, REQUEST_MAP);

        showFragment("Detail", fragment, MAP_TAG);
    }

    @Override
    public void onSaveButtonClick() {
        Intent intent = new Intent(getActivity(), ValidityDateActivity.class);
        startActivityForResult(intent, REQUEST_VALIDITY_DATE);
        getActivity().overridePendingTransition(R.anim.slide_up_dialog, R.anim.no_change);
    }

    private void save(Calendar validityDate){

        Map<String, String> formValues = getFormValues();

        SaveFormTask.FormData formData = new SaveFormTask.FormData(formValues,
                getArguments().getLongArray(ARG_RECORDS_ID),
                getLastLocation(), validityDate);

        save(formData);
    }

}
