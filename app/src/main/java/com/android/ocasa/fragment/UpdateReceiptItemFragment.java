package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.loader.ReceiptRecordTaskLoader;
import com.android.ocasa.loader.ReceiptRecordTaskLoaderTest;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.Map;

/**
 * Ignacio Oviedo on 31/03/16.
 */
public class UpdateReceiptItemFragment extends FormRecordFragment {

    static final String ARG_AVAILABLE_COLUMNS = "available_columns";

    public static UpdateReceiptItemFragment newInstance(ArrayList<String> availableColumns, long recordId) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_AVAILABLE_COLUMNS, availableColumns);
        args.putLong(ARG_RECORD_ID, recordId);

        UpdateReceiptItemFragment fragment = new UpdateReceiptItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
        return new ReceiptRecordTaskLoaderTest(getActivity(),
                args.getLong(ARG_RECORD_ID),
                args.getStringArrayList(ARG_AVAILABLE_COLUMNS));
    }

    @Override
    public void fillForm(FormViewModel record) {

        if(ReceiptCacheManager.getInstance().recordExists(record.getId())){
            fillFromCache();
            return;
        }

        fillFields(new ArrayList<>(record.getFields()));
    }

    private void fillFromCache(){

        FormViewModel record = getRecord();

        //ReceiptCacheManager.getInstance().fillRecord(record);

        fillFields(record.getFields());
    }

    @Override
    public void onSaveButtonClick() {

        FormViewModel record = getRecord();

        Map<String, String> formValues = getFormValues();

        for (FieldViewModel field : record.getFields()){
            field.setValue(formValues.get(field.getTag()));
        }

        //ReceiptCacheManager.getInstance().saveRecord(record);

        getActivity().onBackPressed();
    }
}
