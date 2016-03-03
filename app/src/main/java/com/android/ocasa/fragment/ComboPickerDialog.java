package com.android.ocasa.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.loader.TableTaskLoader;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 22/02/16.
 */
public class ComboPickerDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Table>{

    static final String ARG_TABLE_ID = "table_id";
    static final String COMBO_FIELD_TAG = "field_tag";

    private PickerAdapter adapter;

    private OnComboPickerListener callback;

    public interface OnComboPickerListener{
        void onPick(String fieldTag, Record record);
    }

    public static ComboPickerDialog newInstance(String fieldTag, String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);
        args.putString(COMBO_FIELD_TAG, fieldTag);

        ComboPickerDialog fragment = new ComboPickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnComboPickerListener) getParentFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().getClass().getName() + " must implements OnComboPickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        adapter = new PickerAdapter();

        return new AlertDialog.Builder(getActivity(), R.style.SlideDialog)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onPick(getArguments().getString(COMBO_FIELD_TAG),
                                (Record) adapter.getItem(i));
                    }
                }).create();
    }

    @Override
    public Loader<Table> onCreateLoader(int id, Bundle args) {
        return new TableTaskLoader(getActivity(), args.getString(ARG_TABLE_ID), null);
    }

    @Override
    public void onLoadFinished(Loader<Table> loader, Table data) {
        if(!data.getRecords().isEmpty()) {
            adapter.setRecords(new ArrayList<>(data.getRecords()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Table> loader) {

    }

    public static class PickerAdapter extends BaseAdapter{

        private List<Record> records;

        public PickerAdapter(){
            this.records = new ArrayList<>();
        }

        public void setRecords(List<Record> records){
            this.records.addAll(records);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return records.size();
        }

        @Override
        public Object getItem(int i) {
            return records.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            RecordHolder holder;

            if(view == null){
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_record, viewGroup, false);

                holder = new RecordHolder();

                holder.first = (TextView) view.findViewById(R.id.first);
                holder.second = (TextView) view.findViewById(R.id.second);
                holder.third = (TextView) view.findViewById(R.id.third);

                view.setTag(holder);
            }else{
                holder = (RecordHolder) view.getTag();
            }

            Record record = records.get(position);

            List<Field> fields = new ArrayList<>(record.getFields());

            holder.first.setText(fields.get(0).getValue());
            holder.second.setText(fields.get(1).getValue());
            holder.third.setText(fields.get(2).getValue());

            return view;
        }

        static class RecordHolder{

            TextView first;
            TextView second;
            TextView third;
        }
    }
}
