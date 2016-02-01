package com.android.ocasa.core.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class MultiItemAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private SparseArray<DelegateAdapter<T>> adapters;

    private List<AdapterItem> items;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return adapters.get(viewType).onCreateViewHolder();
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        adapters.get(items.get(position).getType()).onBindViewHolder(holder, items.get(position));
    }

    public void addAdapter(DelegateAdapter<T> adapter, int type){
        adapters.put(type, adapter);
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
