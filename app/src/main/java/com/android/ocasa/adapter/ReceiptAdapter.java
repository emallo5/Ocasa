package com.android.ocasa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Receipt;

import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class ReceiptAdapter extends BaseAdapter {

    private List<Receipt> receipts;

    public ReceiptAdapter(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int i) {
        return receipts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return receipts.get(i).getId();
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

        Receipt receipt = receipts.get(position);

        holder.first.setText(String.valueOf(receipt.getNumber()));
        holder.second.setText(receipt.getValidityDate());

        return view;
    }

    static class RecordHolder{

        TextView first;
        TextView second;
        TextView third;
    }
}
