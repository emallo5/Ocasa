package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.loader.RecordTaskLoader;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.util.DatePickerDialogFragment;
import com.android.ocasa.widget.FieldMapView;
import com.android.ocasa.widget.FieldViewActionListener;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class DetailRecordFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Record>, FieldViewActionListener{

    static final String ARG_RECORD_ID = "record_id";

    private LinearLayout container;

    private TextView tableTitle;

    public static DetailRecordFragment newInstance(int recordId) {

        Bundle args = new Bundle();
        args.putInt(ARG_RECORD_ID, recordId);

        DetailRecordFragment fragment = new DetailRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = (LinearLayout) view.findViewById(R.id.detail_container);
        tableTitle = (TextView) view.findViewById(R.id.table_title);
    }

    @Override
    public Loader<Record> onCreateLoader(int id, Bundle args) {
        return new RecordTaskLoader(getActivity(), args.getInt(ARG_RECORD_ID));
    }

    @Override
    public void onLoadFinished(Loader<Record> loader, Record data) {

        if(data != null){
            fillRecord(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Record> loader) {

    }

    private void fillRecord(Record record){

        tableTitle.setText(record.getTable().getName().toUpperCase());

        fillFields(new ArrayList<Field>(record.getFields()));
    }

    private void fillFields(List<Field> fields){

        if(container.getChildCount() > 1)
            container.removeViews(1, fields.size());

        for (Field field : fields){
            FieldViewFactory factory = field.getColumn().getFieldType().getFieldFactory();

            View view = factory.createView(container, field);

            if(view != null) {
                try {
                    view.setTag(field.getColumn().getId());

                    FieldViewAdapter adapter = (FieldViewAdapter) view;
                    adapter.setFieldViewActionListener(this);
                } catch (ClassCastException e) {

                }

                container.addView(view);
            }
        }
    }

    @Override
    public void onMapClick(FieldMapView view, LatLng location) {
        ((BarActivity) getActivity())
                .pushFragment(Fragment.instantiate(getActivity(), MapFragment.class.getName()), "Map", true, true);
    }

    @Override
    public void onDateClick() {

        DatePickerDialogFragment datePickerDialog = DatePickerDialogFragment.newInstance();
        datePickerDialog.show(getChildFragmentManager(), "DateDialog");
    }

    @Override
    public void onComboClick() {

    }

}
