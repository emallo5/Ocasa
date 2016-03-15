package com.android.ocasa.core.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ignacio on 07/03/16.
 */
public interface DelegateListAdapter {

    public View createView(View convertView, ViewGroup parent, int position);
    public int getViewType();
    public Object getItem();

}
