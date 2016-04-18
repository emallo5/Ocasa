package com.android.ocasa.adapter;

import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.event.RecordLongClickEvent;
import com.android.ocasa.viewmodel.CellViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class RecordAdapterTest extends RecyclerView.Adapter<RecordAdapterTest.RecordViewHolder> {

    private LongSparseArray<Boolean> selectedItems;

    private List<CellViewModel> records;

    private int fieldCount;

    public RecordAdapterTest(List<CellViewModel> records) {
        this.records = records;

        fieldCount = records.isEmpty() ? 0 : records.get(0).getFields().size();

        selectedItems = new LongSparseArray<>();

        setHasStableIds(true);
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_receipt_item_detail, parent, false), fieldCount);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        CellViewModel record = records.get(position);

        holder.itemView.setActivated(selectedItems.get(record.getId(), false));

        for (int index = 0; index < holder.fields.size(); index++){
            TextView text = holder.fields.get(index);
            text.setText(record.getFields().get(index).getValue());
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

        ArrayList<TextView> fields;

        public RecordViewHolder(View itemView, int fieldCount) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

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

        @Override
        public boolean onLongClick(View view) {
            EventBus.getDefault().post(new RecordLongClickEvent(getAdapterPosition()));
            return true;
        }
    }
}
