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

    private List<CellViewModel> records;

    private int fieldCount;

    public AvailableItemsAdapter(List<CellViewModel> records) {
        this.records = records;

        fieldCount = records.isEmpty() ? 0 : records.get(0).getFields().size();

        selectedItems = new LongSparseArray<>();

        setHasStableIds(true);
    }

    public void addItem(CellViewModel item){
        records.add(0, item);
        notifyItemInserted(0);
    }

    public void deleteItem(int position){
        records.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_receipt_item_detail, parent, false), fieldCount);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        CellViewModel record = records.get(position);

        if(record.isNew()){
            holder.status.setImageResource(R.drawable.ic_new_record);
        }else if(record.isUpdated()){
            holder.status.setImageResource(R.drawable.ic_update_record);
        }else {
            holder.status.setVisibility(View.GONE);
        }

        holder.itemView.setActivated(selectedItems.get(record.getId(), false));

        for (int index = 0; index < holder.fields.size(); index++){
            FieldViewModel field = record.getFields().get(index);

            TextView text = holder.fields.get(index);
            text.setText(field.getLabel() + ": " + field.getValue());

            if(field.isHighlight()){
                text.setTypeface(null, Typeface.BOLD);
                text.setTextSize(18);
            }else{
                text.setTypeface(null, Typeface.NORMAL);
                text.setTextSize(14);
            }
        }
    }

    public void toggleSelection(int pos) {

        long id = getItemId(pos);

        if (selectedItems.get(id, false)) {
            selectedItems.delete(id);
        } else {
            selectedItems.put(id, true);
        }

        notifyItemChanged(pos);
    }

    public void setSelected(int pos) {
        selectedItems.put(getItemId(pos), true);
        notifyItemChanged(pos);
    }

    public void clearSelection(int pos) {

        long id = getItemId(pos);

        if (selectedItems.get(id, false)) {
            selectedItems.delete(id);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        if (selectedItems.size() > 0) {
            selectedItems.clear();
            notifyDataSetChanged();
        }
    }


    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void refreshItems(List<CellViewModel> records) {
        fieldCount = records.isEmpty() ? 0 : records.get(0).getFields().size();

        this.records.clear();
        this.records.addAll(records);
        notifyDataSetChanged();
    }

    public long[] getSelectedItemIds() {
        long[] ids = new long[selectedItems.size()];
        for (int i = 0; i < selectedItems.size(); i++) {
            ids[i]= selectedItems.keyAt(i);
        }
        return ids;
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
                text.setTextColor(Color.WHITE);

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
