package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 11/02/16.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoryHolder> {

    private List<History> historical;

    public HistoricalAdapter(List<History> historical) {
        this.historical = historical;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_record, parent, false));

    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        History history = historical.get(position);

        holder.first.setText(history.getDate());
        holder.second.setText(history.getValue());
        holder.third.setText("Ignacio Oviedo");
    }

    @Override
    public int getItemCount() {
        return historical.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{

        TextView first;
        TextView second;
        TextView third;

        public HistoryHolder(View itemView) {
            super(itemView);

            first = (TextView) itemView.findViewById(R.id.first);
            second = (TextView) itemView.findViewById(R.id.second);
            third = (TextView) itemView.findViewById(R.id.third);
        }
    }
}
