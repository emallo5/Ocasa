package com.android.ocasa.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ocasa.core.adapter.DelegateListAdapter;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 04/02/16.
 */
public class MenuOptionsAdapter extends BaseAdapter {

    private List<DelegateListAdapter> items;

    public MenuOptionsAdapter(List<Category> categories){

        items = new ArrayList<>();

        for (Category category : categories){
            items.add(new CategoryAdapter(category));

            for (Table table : category.getTables()){
                items.add(new TableAdapter(table));
            }
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i).getItem();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return items.get(i).createView(view, viewGroup, i);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }
}
