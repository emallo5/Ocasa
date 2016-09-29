package com.android.ocasa.record.create;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.core.FormFragment;
import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.loader.CreateFormTask;
import com.android.ocasa.loader.SaveFormTask;
import com.android.ocasa.record.invoke.InvokeRecordActivity;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.Map;

/**
 * Created by ignacio on 14/07/16.
 */
public class CreateRecordFragment extends FormFragment {

    static final String ARG_TABLE_ID = "table_id";

    public static CreateRecordFragment newInstance(String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);

        CreateRecordFragment fragment = new CreateRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Loader<FormPresenter> getPresenterLoader() {
        return new CreateRecordLoader(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CreateRecordPresenter)getPresenter()).load(getArguments().getString(ARG_TABLE_ID));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_form_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.invoke){
            Intent invoke = new Intent(getActivity(), InvokeRecordActivity.class);
            invoke.putExtra(InvokeRecordActivity.EXTRA_TABLE_ID, getArguments().getString(ARG_TABLE_ID));
            startActivityForResult(invoke, 1000);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFormSuccess(FormViewModel form) {

        form.getFields().get(0).setValue("");
        form.getFields().get(0).setEditable(true);
//        super.onFormSuccess(form);
        setRecordForm(form);
        setTitle("Nuevo " + form.getTitle());

        fillFields(form.getFields(), true);

        container.setCardBackgroundColor(Color.parseColor(form.getColor()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        if(requestCode == 1000){
            long recordId = data.getLongExtra("record_id", -1);
            getPresenter().load(recordId);
        }
    }

    @Override
    public void onSaveButtonClick() {
        Map<String, String> formValues = getFormValues();

        SaveFormTask.FormData formData = new SaveFormTask.FormData(formValues, -1, getLastLocation());
        formData.setTableId(getArguments().getString(ARG_TABLE_ID));

        new CreateFormTask(getActivity()).execute(formData);

        Toast.makeText(getContext(), "Enviando...", Toast.LENGTH_SHORT).show();

        getActivity().onBackPressed();
    }
}
