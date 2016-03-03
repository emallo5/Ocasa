package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.History;

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
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history, parent, false));

    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        History history = historical.get(position);

        holder.value.setText(history.getValue());
        holder.date.setText(history.getSystemDate() + " " + history.getTimeZone());
        holder.location.setText("Lat : " + history.getLatitude() + " Lng : " + history.getLongitude());
        holder.user.setText("Ignacio Oviedo");
    }

    @Override
    public int getItemCount() {
        return historical.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder{

        TextView value;
        TextView date;
        TextView location;
        TextView user;

        public HistoryHolder(View itemView) {
            super(itemView);

            value = (TextView) itemView.findViewById(R.id.value);
            date = (TextView) itemView.findViewById(R.id.date);
            location = (TextView) itemView.findViewById(R.id.location);
            user = (TextView) itemView.findViewById(R.id.user);
        }
    }
}
