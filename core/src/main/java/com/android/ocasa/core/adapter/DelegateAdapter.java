package com.android.ocasa.core.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ignacio on 18/01/16.
 */
public interface DelegateAdapter<T extends RecyclerView.ViewHolder> {

    public T onCreateViewHolder();
    public void onBindViewHolder(T holder, AdapterItem item);
}
