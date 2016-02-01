package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {

    private List<Record> records;

    public RecordAdapter(List<Record> records) {
        this.records = records;
        setHasStableIds(true);
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_record, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {

        Record record = records.get(position);

        List<Field> fields = new ArrayList<>(record.getFields());

        holder.first.setText(fields.get(0).getValue());
        holder.second.setText(fields.get(1).getValue());
        holder.third.setText(fields.get(2).getValue());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

    public class RecordHolder extends RecyclerView.ViewHolder{

        TextView first;
        TextView second;
        TextView third;

        public RecordHolder(View itemView) {
            super(itemView);

            first = (TextView) itemView.findViewById(R.id.first);
            second = (TextView) itemView.findViewById(R.id.second);
            third = (TextView) itemView.findViewById(R.id.third);
        }

    }

}
