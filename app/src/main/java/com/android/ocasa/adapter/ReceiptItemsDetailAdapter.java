package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.event.ReceiptItemDeleteEvent;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ReceiptItemsDetailAdapter extends RecyclerView.Adapter<ReceiptItemsDetailAdapter.ItemViewHolder> {

    private List<CellViewModel> records;

    private int fieldCount;

    public ReceiptItemsDetailAdapter(List<CellViewModel> records) {
        this.records = records;

        fieldCount = records.get(0).getFields().size();

        setHasStableIds(true);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_receipt_item_detail, parent, false), fieldCount);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        CellViewModel record = records.get(position);

        for (int index = 0; index < holder.fields.size(); index++){
            TextView text = holder.fields.get(index);
            text.setText(record.getFields().get(index).getValue());
        }
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ArrayList<TextView> fields;

        public ItemViewHolder(View itemView, int fieldCount) {
            super(itemView);

            itemView.setOnClickListener(this);

            fields = new ArrayList<>();

            LinearLayout container = (LinearLayout) itemView.findViewById(R.id.container);

            for (int index = 0; index < fieldCount; index++) {
                TextView text = new TextView(itemView.getContext());
                fields.add(text);
                container.addView(text);
            }
        }

        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new ReceiptItemEvent(getItemId(), getAdapterPosition()));
        }
    }
}
