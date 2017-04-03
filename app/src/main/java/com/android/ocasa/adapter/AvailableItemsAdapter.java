package com.android.ocasa.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.event.ReceiptItemAddEvent;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.event.RecordLongClickEvent;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 11/07/16.
 */
public class AvailableItemsAdapter extends RecyclerView.Adapter<AvailableItemsAdapter.RecordViewHolder> {

    private LongSparseArray<Boolean> selectedItems;

    private ArrayList<CellViewModel> records = new ArrayList<>();
    private ArrayList<CellViewModel> recordsBackup = new ArrayList<>();

    private int fieldCount;

    public AvailableItemsAdapter(List<CellViewModel> records) {
        this.records.addAll(records);
        this.recordsBackup.addAll(records);

        fieldCount = records.isEmpty() ? 0 : records.get(0).getFields().size();

        selectedItems = new LongSparseArray<>();

        setHasStableIds(true);
    }

    public void addItem(CellViewModel item){
        records.add(0, item);
        notifyDataSetChanged();
    }

    public void deleteItem(long id) {

        for (int i=0; i<records.size(); i++) {
            if (id == records.get(i).getId()) {
                records.remove(i);
                break;
            }
        }
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_receipt_item_detail, parent, false), fieldCount);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        CellViewModel record = records.get(position);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        if (record.isNew()) {
            holder.status.setImageResource(R.drawable.ic_new_record);
        } else if (record.isUpdated()) {
            holder.status.setImageResource(R.drawable.ic_update_record);
        } else {
            holder.status.setVisibility(View.GONE);
        }

        holder.itemView.setActivated(selectedItems.get(record.getId(), false));

        for (int index = 0; index < holder.fields.size(); index++){

            FieldViewModel field = record.getFields().get(index);
            TextView text = holder.fields.get(index);
            if (!field.isEditable() && field.isVisible()) {
                text.setText(field.getLabel() + ": " + field.getValue());
                text.setVisibility(View.VISIBLE);
            } else {
                text.setVisibility(View.GONE);
            }
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

    public CellViewModel getItem(int position) {
        return records.get(position);
    }

    public void refreshItems(List<CellViewModel> records) {
        fieldCount = records.isEmpty() ? 0 : records.get(0).getFields().size();

        this.records.clear();
        this.records.addAll(records);
        this.recordsBackup.clear();
        this.recordsBackup.addAll(records);
        notifyDataSetChanged();
    }

    public int filter(String filter) {
        // retorna true si hay un solo item matcheado, de esta manera, vamos a la carga de POD directamente

        records.clear();

        if (filter.isEmpty() || filter.length() == 0) {
            records.addAll(recordsBackup);
            notifyDataSetChanged();
            return records.size();
        }

        for (CellViewModel cell : recordsBackup)
            for (FieldViewModel field : cell.getFields())
                    if (field.getValue().toLowerCase().contains(filter.toLowerCase())) {
                        records.add(cell);
                        break;
                    }
        notifyDataSetChanged();

        return records.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener{

        ImageView status;

        ArrayList<TextView> fields;

        public RecordViewHolder(View itemView, int fieldCount) {
            super(itemView);

            status = (ImageView) itemView.findViewById(R.id.record_state);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            fields = new ArrayList<>();

            LinearLayout container = (LinearLayout) itemView.findViewById(R.id.container);

            for (int index = 0; index < fieldCount; index++) {
                TextView text = new TextView(itemView.getContext());
                text.setTextColor(Color.BLACK);
                text.setTypeface(null, Typeface.BOLD);

                fields.add(text);
                container.addView(text);
            }
        }

        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new ReceiptItemAddEvent(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            EventBus.getDefault().post(new RecordLongClickEvent(getAdapterPosition()));
            return true;
        }
    }
}
