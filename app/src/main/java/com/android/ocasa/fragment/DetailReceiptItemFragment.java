package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.android.ocasa.loader.DetailReceiptRecordTaskLoader;
import com.android.ocasa.loader.DetailReceiptRecordTaskLoaderTest;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;

/**
 * Ignacio Oviedo on 11/04/16.
 */
public class DetailReceiptItemFragment extends FormRecordFragment {

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
    public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
        return new DetailReceiptRecordTaskLoaderTest(getActivity(),
                args.getLong(ARG_RECORD_ID),
                args.getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void fillForm(FormViewModel record) {
        fillFields(new ArrayList<>(record.getFields()));
    }

    @Override
    public void onSaveButtonClick() {

    }
}
