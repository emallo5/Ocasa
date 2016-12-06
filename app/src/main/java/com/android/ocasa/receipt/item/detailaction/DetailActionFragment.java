package com.android.ocasa.receipt.item.detailaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.List;

/**
 * Created by ignacio on 24/10/16.
 */
public class DetailActionFragment extends FormFragment{

    static String ARG_RECEIPT_ID = "receipt_id";
    static String ARG_RECORD_ID = "record_id";

    public static DetailActionFragment newInstance(long receiptId, long recordId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);
        args.putLong(ARG_RECORD_ID, recordId);

        DetailActionFragment fragment = new DetailActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailActionPresenter)getPresenter()).loadFields(getArguments().getLong(ARG_RECORD_ID), getArguments().getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void onFormSuccess(FormViewModel form) {
        super.onFormSuccess(form);
        container.setCardBackgroundColor(Color.WHITE);
        showFieldsInfo();
    }

    @Override
    public void fillFields(List<FieldViewModel> fields, boolean isEditMode) {
        if(formContainer.getChildCount() > 0)
            return;

        super.fillFields(fields, isEditMode);
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new DetailActionLoader(getActivity());
    }

    @Override
    public void onSaveButtonClick() {

        SaveFormTask.FormData data =
                new SaveFormTask.FormData(getFormValues(),
                        getArguments().getLong(ARG_RECORD_ID),
                        getLastLocation());

        save(data);

        getActivity().setResult(Activity.RESULT_OK,
                new Intent().putExtra(EditReceiptFragment.RECORD_ID, getArguments().getLong(ARG_RECORD_ID)));
        getActivity().finish();
    }
}
