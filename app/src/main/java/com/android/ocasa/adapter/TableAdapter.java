package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.AdapterItem;
import com.android.ocasa.core.adapter.DelegateAdapter;
import com.android.ocasa.model.Table;

/**
 * Created by ignacio on 10/02/16.
 */
public class TableAdapter implements DelegateAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TableHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_table, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, AdapterItem item) {

        TableHolder tableHolder = (TableHolder) holder;

        Table app = (Table) item.getData();

        tableHolder.name.setText(app.getName());

    }

    public class TableHolder extends RecyclerView.ViewHolder{

        TextView name;

        public TableHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
