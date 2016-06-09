package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.event.ReceiptHistoryClickEvent;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by ignacio on 11/02/16.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoryHolder> {

    private List<History> historical;

    public HistoricalAdapter(List<History> historical) {
        this.historical = historical;

        setHasStableIds(true);
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

        Receipt receipt = history.getReceipt();

        if(receipt != null){
            holder.receiptNumber.setVisibility(View.VISIBLE);
            holder.receiptNumber.setText("Comprobante: " + receipt.getNumber());
        }else{
            holder.receiptNumber.setVisibility(View.GONE);
        }

        holder.date.setText(history.getSystemDate() + " " + history.getTimeZone());
        holder.location.setText("Lat : " + history.getLatitude() + " Lng : " + history.getLongitude());
        holder.user.setText("Ignacio Oviedo");
    }

    @Override
    public int getItemCount() {
        return historical.size();
    }

    @Override
    public long getItemId(int position) {
        return historical.get(position).getId();
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView value;
        TextView receiptNumber;
        TextView date;
        TextView location;
        TextView user;

        public HistoryHolder(View itemView) {
            super(itemView);

            value = (TextView) itemView.findViewById(R.id.value);
            receiptNumber = (TextView) itemView.findViewById(R.id.receipt_number);
            receiptNumber.setOnClickListener(this);
            date = (TextView) itemView.findViewById(R.id.date);
            location = (TextView) itemView.findViewById(R.id.location);
            user = (TextView) itemView.findViewById(R.id.user);
        }

        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new ReceiptHistoryClickEvent((int) getItemId()));
        }
    }
}
