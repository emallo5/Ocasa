package com.android.ocasa.receipt.status;

import android.graphics.Color;
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
import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.PairViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 26/05/16.
 */
public class ReceiptStatusFragment extends FormFragment {

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
    public void onFormSuccess(FormViewModel form) {
        setTitle(form.getTitle());
        container.setCardBackgroundColor(Color.parseColor(form.getColor()));
        fillStatus(form.getStatus(), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter()
                .load(getArguments().getLong(ARG_RECEIPT_ID));
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new ReceiptStatusLoader(getActivity());
    }

    public void fillStatus(List<PairViewModel> fields, boolean isEditMode){

        if(formContainer.getChildCount() > 0){
            formContainer.removeAllViewsInLayout();
        }

        Location lastLocation = getLastLocation();

        if(lastLocation != null) {
            TextView location = new TextView(getActivity());
            location.setTextColor(Color.WHITE);
            location.setText(lastLocation.getLatitude() + " " + lastLocation.getLongitude());

            formContainer.addView(location);
        }

        TextView time = new TextView(getActivity());
        time.setTextColor(Color.WHITE);
        time.setText(DateTimeHelper.formatDateTime(new Date()) + " " + DateTimeHelper.getDeviceTimezone());
        formContainer.addView(time);

        for (int index = 0; index < fields.size(); index++){

            LinearLayout pair = (LinearLayout)
                    LayoutInflater.from(getContext()).inflate(R.layout.pair_status_layout, formContainer, false);

            FieldViewModel firstField = fields.get(index).getFirstField();

            FieldViewFactory factory = firstField.getType().getFieldFactory();

            View firstView = factory.createView(formContainer, firstField, isEditMode);

            pair.addView(firstView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            if(fields.get(index).getSecondField() != null){
                FieldViewModel secondField = fields.get(index).getSecondField();

                factory = secondField.getType().getFieldFactory();

                View secondView = factory.createView(formContainer, secondField, isEditMode);

                pair.addView(secondView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            }

//            if(view != null) {
//                formValues.put(field.getTag(), field.getValue());
//                view.setTag(field.getTag());
//
//                FieldViewAdapter adapter = (FieldViewAdapter) view;
//                adapter.setFieldViewActionListener(this);
            formContainer.addView(pair);
//            }
        }
    }

    @Override
    public void onSaveButtonClick() {

    }
}
