package com.android.ocasa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class RecordAdapter extends BaseAdapter {

    private List<Record> records;

    public RecordAdapter(List<Record> records) {
        this.records = records;
    }

    public void refreshItems(ArrayList<Record> data) {
        this.records.clear();
        this.records.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return true;
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
    public long getItemId(int position) {
        return records.get(position).getId();
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
