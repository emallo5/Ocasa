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
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.event.RecordLongClickEvent;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 11/04/16.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int RECORD_TYPE = 1;

    private LongSparseArray<Boolean> selectedItems;

    private TableViewModel table;

    private int fieldCount;

    public RecordAdapter(TableViewModel table) {
        this.table = table;

        fieldCount = table.getCells().isEmpty() ? 0 : table.getCells().get(0).getFields().size();

        selectedItems = new LongSparseArray<>();

        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == HEADER_TYPE){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_record_header, parent, false), table.getHeader().getFields().size());
        }

        return new RecordViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_receipt_item_detail, parent, false), fieldCount);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == HEADER_TYPE){
            bindHeaderHolder(table.getHeader(), (HeaderViewHolder) holder);
        }else{
            bindRecordHolder(table.getCells().get(position - 1), (RecordViewHolder) holder);
        }
    }

    private void bindRecordHolder(CellViewModel record, RecordViewHolder holder){

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

    private void bindHeaderHolder(FormViewModel header, HeaderViewHolder holder){
        holder.fields.get(0).setText(header.getFields().get(0).getValue());
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_TYPE : RECORD_TYPE;
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position) == HEADER_TYPE ? - 1 : table.getCells().get(position - 1).getId();
    }

    @Override
    public int getItemCount() {
        return table.getCells().size() + 1;
    }

    public void refreshItems(TableViewModel table) {
        fieldCount = table.getCells().isEmpty() ? 0 : table.getCells().get(0).getFields().size();

        this.table.getCells().clear();
        this.table.getCells().addAll(table.getCells());
        notifyDataSetChanged();
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
            EventBus.getDefault().post(new ReceiptItemEvent(getItemId(), getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            EventBus.getDefault().post(new RecordLongClickEvent(getAdapterPosition()));
            return true;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        ArrayList<TextView> fields;

        public HeaderViewHolder(View itemView, int fieldCount) {
            super(itemView);

            fields = new ArrayList<>();

            LinearLayout container = (LinearLayout) itemView.findViewById(R.id.container);

            for (int index = 0; index < fieldCount; index++) {
                TextView text = new TextView(itemView.getContext());
                text.setTextColor(Color.WHITE);
                text.setTypeface(null, Typeface.BOLD);
                text.setTextSize(18);

                fields.add(text);
                container.addView(text);
            }
        }
    }
}
