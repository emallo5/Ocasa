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
import com.android.ocasa.loader.DetailReceiptStatusTaskLoader;
import com.android.ocasa.loader.ReceiptStatusTaskLoader;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.PairViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 02/06/16.
 */
public class DetailReceiptStatusFragment extends FormRecordFragment {

    static final String ARG_RECEIPT_ID = "receipt_id";

    public static DetailReceiptStatusFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        DetailReceiptStatusFragment fragment = new DetailReceiptStatusFragment();
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
        return new DetailReceiptStatusTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void fillForm(FormViewModel record) {

        setTitle(record.getTitle());

        fillStatus(record.getStatus(), false);
    }


    public void fillStatus(List<PairViewModel> fields, boolean isEditMode){

        for (int index = 0; index < fields.size(); index++){

            LinearLayout pair = (LinearLayout)
                    LayoutInflater.from(getContext()).inflate(R.layout.pair_status_layout, container, false);

            FieldViewModel firstField = fields.get(index).getFirstField();

            FieldViewFactory factory = firstField.getType().getFieldFactory();

            View firstView = factory.createView(container, firstField, isEditMode);

            pair.addView(firstView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            if(firstView!= null) {
                formValues.put(firstField.getTag(), firstField.getValue());
                firstView.setTag(firstField.getTag());
            }

            if(fields.get(index).getSecondField() != null){
                FieldViewModel secondField = fields.get(index).getSecondField();

                factory = secondField.getType().getFieldFactory();

                View secondView = factory.createView(container, secondField, isEditMode);

                pair.addView(secondView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                if(secondView!= null) {
                    formValues.put(secondField.getTag(), secondField.getValue());
                    secondView.setTag(secondField.getTag());
                }
            }

            container.addView(pair);
        }
    }

    @Override
    public void onSaveButtonClick() {

    }
}
