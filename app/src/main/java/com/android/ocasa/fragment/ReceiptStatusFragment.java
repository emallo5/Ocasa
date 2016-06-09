package com.android.ocasa.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.loader.ReceiptStatusTaskLoader;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.PairViewModel;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.DateTimeFieldFactory;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 26/05/16.
 */
public class ReceiptStatusFragment extends FormRecordFragment {

    static final String ARG_RECEIPT_ID = "receipt_id";

    public static ReceiptStatusFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        ReceiptStatusFragment fragment = new ReceiptStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
    }

    @Override
    public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
        return new ReceiptStatusTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void fillForm(FormViewModel record) {

        setTitle(record.getTitle());

        fillStatus(record.getStatus(), false);
    }


    public void fillStatus(List<PairViewModel> fields, boolean isEditMode){

//        if(container.getChildCount() > 1)
//            container.removeViews(1, fields.size() + 1);

        Location lastLocation = getLastLocation();

        if(lastLocation != null) {
            TextView location = new TextView(getActivity());
            location.setText(lastLocation.getLatitude() + " " + lastLocation.getLongitude());

            container.addView(location);
        }

        TextView time = new TextView(getActivity());
        time.setText(DateTimeHelper.formatDateTime(new Date()) + " " + DateTimeHelper.getDeviceTimezone());
        container.addView(time);

        for (int index = 0; index < fields.size(); index++){

            LinearLayout pair = (LinearLayout)
                    LayoutInflater.from(getContext()).inflate(R.layout.pair_status_layout, container, false);

            FieldViewModel firstField = fields.get(index).getFirstField();

            FieldViewFactory factory = firstField.getType().getFieldFactory();

            View firstView = factory.createView(container, firstField, isEditMode);

            pair.addView(firstView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            if(fields.get(index).getSecondField() != null){
                FieldViewModel secondField = fields.get(index).getSecondField();

                factory = secondField.getType().getFieldFactory();

                View secondView = factory.createView(container, secondField, isEditMode);

                pair.addView(secondView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            }

//            if(view != null) {
//                formValues.put(field.getTag(), field.getValue());
//                view.setTag(field.getTag());
//
//                FieldViewAdapter adapter = (FieldViewAdapter) view;
//                adapter.setFieldViewActionListener(this);
                container.addView(pair);
//            }
        }
    }

    @Override
    public void onSaveButtonClick() {

    }
}
