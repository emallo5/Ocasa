package com.android.ocasa.core.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ignacio on 18/01/16.
 */
public interface DelegateAdapter {

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);
    public void onBindViewHolder(RecyclerView.ViewHolder holder, AdapterItem item);
}
