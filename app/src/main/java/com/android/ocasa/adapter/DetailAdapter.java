package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 25/02/16.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailHolder> {

    //TODO Ver creación dinamica vistas
    private List<Record> records;

    private int fieldCount;

    public DetailAdapter(List<Record> records) {
        this.records = records;
        this.fieldCount = records.get(0).getFields().size();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailHolder(R.layout.adapter_detail, parent, fieldCount);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {

        Record record = records.get(position);

        List<Field> fields = new ArrayList<>(record.getFields());

        for (int index = 0; index < holder.views.size(); index++){
            holder.views.get(index).setText(fields.get(index).getColumn().getName() + ": " + fields.get(index).getValue());
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class DetailHolder extends RecyclerView.ViewHolder{

        List<TextView> views;

        public DetailHolder(int resId, ViewGroup parent, int viewCount) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));

            views = new ArrayList<>();

            LinearLayout container = (LinearLayout) itemView;

             for (int index  = 0; index < viewCount; index++){
                 TextView text = new TextView(parent.getContext());

                 views.add(text);
                 container.addView(text);
             }
        }
    }

}
