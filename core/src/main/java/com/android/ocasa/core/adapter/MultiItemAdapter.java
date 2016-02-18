package com.android.ocasa.core.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public abstract class MultiItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArray<DelegateAdapter> adapters;

    private List<AdapterItem> items;

    public MultiItemAdapter(){
        this.adapters = new SparseArray<>();
        this.items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return adapters.get(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        adapters.get(items.get(position).getType()).onBindViewHolder(holder, items.get(position));
    }

    public void addAdapter(DelegateAdapter adapter, int type){
        adapters.put(type, adapter);
    }

    public void addItem(AdapterItem item){
        items.add(item);
    }

    public AdapterItem getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
