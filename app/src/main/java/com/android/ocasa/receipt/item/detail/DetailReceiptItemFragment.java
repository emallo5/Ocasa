package com.android.ocasa.receipt.item.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.android.ocasa.R;
import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.viewmodel.FormViewModel;

/**
 * Ignacio Oviedo on 11/04/16.
 */
public class DetailReceiptItemFragment extends FormFragment {

    static final String ARG_RECORD_ID = "record_id";
    static final String ARG_RECEIPT_ID = "receipt_id";

    public static DetailReceiptItemFragment newInstance(long recordId, long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId);
        args.putLong(ARG_RECEIPT_ID, receiptId);

        DetailReceiptItemFragment fragment = new DetailReceiptItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((DetailReceiptItemPresenter)getPresenter())
                .load(getArguments().getLong(ARG_RECORD_ID), getArguments().getLong(ARG_RECEIPT_ID));
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new DetailReceiptItemLoader(getActivity());
    }

    @Override
    public void onFormSuccess(FormViewModel form) {
        super.onFormSuccess(form);
        container.setCardBackgroundColor(Color.parseColor(form.getColor()));
        setTitle(getString(R.string.detail_title, form.getTitle()));
    }

    @Override
    public void onSaveButtonClick() {

    }
}
