package com.android.ocasa.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.event.ReceiptItemDeleteEvent;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ReceiptItemsAdapter extends RecyclerView.Adapter<ReceiptItemsAdapter.ItemViewHolder> {

    private List<CellViewModel> records;

    private int fieldCount;

    public ReceiptItemsAdapter(CellViewModel record) {
        this.records = new ArrayList<>();
        this.records.add(record);

        fieldCount = records.size() == 0 ? 0 : records.get(0).getFields().size();

        setHasStableIds(true);
    }

    public ReceiptItemsAdapter(List<CellViewModel> records) {
        this.records = records;

        fieldCount = records.size() == 0 ? 0 : records.get(0).getFields().size();

        setHasStableIds(true);
    }


    public void refreshItems(List<CellViewModel> records) {
        fieldCount = records.size() == 0 ? 0 : records.get(0).getFields().size();

        this.records.clear();
        this.records.addAll(records);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_receipt_item, parent, false), fieldCount);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        CellViewModel record = records.get(position);

        holder.number.setText("#" + (getItemCount() - position));

        for (int index = 0; index < holder.fields.size(); index++){
            FieldViewModel field = record.getFields().get(index);

            TextView text = holder.fields.get(index);
            text.setText(record.getFields().get(index).getValue());

            if(field.isHighlight()){
                text.setTypeface(null, Typeface.BOLD);
                text.setTextSize(18);
            }else{
                text.setTypeface(null, Typeface.NORMAL);
                text.setTextSize(14);
            }
        }
    }

    public void deleteItem(int position){
        records.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, position);
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void addItem(CellViewModel cellViewModel) {
        records.add(0, cellViewModel);
        cellViewModel.setNumber(records.size());
        notifyItemInserted(0);
    }

    public void addItems(List<CellViewModel> cellViewModel) {
        records.addAll(0, cellViewModel);
//        notifyItemRangeInserted(0, cellViewModel.size());
        notifyDataSetChanged();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView number;
        ImageButton delete;

        ArrayList<TextView> fields;

        public ItemViewHolder(View itemView, int fieldCount) {
            super(itemView);

            itemView.setOnClickListener(this);

            fields = new ArrayList<>();

            number = (TextView) itemView.findViewById(R.id.number);

            LinearLayout container = (LinearLayout) itemView.findViewById(R.id.container);

            for (int index = 0; index < fieldCount; index++) {
                TextView text = new TextView(itemView.getContext());

                fields.add(text);
                container.addView(text);
            }

            delete = (ImageButton) itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.delete:
                    EventBus.getDefault().post(new ReceiptItemDeleteEvent(getItemId(), getAdapterPosition()));
                    break;
                default:
                    EventBus.getDefault().post(new ReceiptItemEvent(getItemId(), getAdapterPosition()));
            }
        }
    }
}
