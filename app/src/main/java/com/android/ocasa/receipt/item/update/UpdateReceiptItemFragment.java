package com.android.ocasa.receipt.item.update;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.android.ocasa.R;
import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.Map;

/**
 * Ignacio Oviedo on 31/03/16.
 */
public class UpdateReceiptItemFragment extends FormFragment {

    static final String ARG_RECORD_ID = "record_id";
    static final String ARG_ACTION_ID = "receipt_id";

    public static UpdateReceiptItemFragment newInstance(long recordId, long actionId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId);
        args.putLong(ARG_ACTION_ID, actionId);

        UpdateReceiptItemFragment fragment = new UpdateReceiptItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((UpdateReceiptItemPresenter)getPresenter())
                .load(getArguments().getLong(ARG_RECORD_ID),
                        getArguments().getLong(ARG_ACTION_ID));
    }

    @Override
    public void onFormSuccess(FormViewModel form) {
        super.onFormSuccess(form);
        if(ReceiptCacheManager.getInstance().recordExists(form.getId())){
            fillFromCache();
            return;
        }

        setTitle(getString(R.string.detail_title, form.getTitle()));
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new UpdateReceiptItemLoader(getActivity());
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
