package com.android.ocasa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.android.ocasa.activity.CreateReceiptActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.loader.EditReceiptTaskLoader;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Emiliano Mallo on 09/05/16.
 */
public class EditReceiptFragment extends FormRecordFragment {

    static final String ARG_ACTION_ID = "action_id";

    public static EditReceiptFragment newInstance(String actionId) {

        EditReceiptFragment frag = new EditReceiptFragment();

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
        return new EditReceiptTaskLoader(getActivity(), args.getString(ARG_ACTION_ID));
    }

    @Override
    public void fillForm(FormViewModel record) {
        setTitle(record.getTitle());

        fillFields(record.getFields(), true);
    }

    @Override
    public void onSaveButtonClick() {
        Receipt receipt = new Receipt();
        receipt.setNumber((int) (Math.random() * 1000));

        Action action = new Action();
        action.setId(getArguments().getString(ARG_ACTION_ID));
        receipt.setAction(action);

        List<Field> fields = new ArrayList<>();

        for (Map.Entry<String, String> pair : getFormValues().entrySet()) {
            Field field = new Field();

            Column column = new Column();
            column.setId(pair.getKey());

            field.setColumn(column);
            field.setValue(pair.getValue());
            field.setReceipt(receipt);

            fields.add(field);
        }

        receipt.setHeaderValues(fields);

        new ReceiptDAO(getActivity()).save(receipt);
        new FieldDAO(getActivity()).save(receipt.getHeaderValues());

        Intent intent = new Intent(getActivity(), CreateReceiptActivity.class);
        intent.putExtra(CreateReceiptActivity.EXTRA_RECEIPT_ID, receipt.getId());

        ((BaseActivity)getActivity()).startNewActivity(intent);
        getActivity().finish();
    }


}
