package com.android.ocasa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.DelegateListAdapter;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.OptionViewModel;

/**
 * Created by ignacio on 10/02/16.
 */
public class TableAdapter implements DelegateListAdapter {

    static final int TABLE_TYPE = 1;

    private OptionViewModel table;

    public TableAdapter(OptionViewModel table) {
        this.table = table;
    }

    @Override
    public View createView(View convertView, ViewGroup parent, int position) {
        TableHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_table, parent, false);

            holder = new TableHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        }else{
            holder = (TableHolder) convertView.getTag();
        }

        holder.name.setText(table.getTitle());

        return convertView;
    }

    @Override
    public int getViewType() {
        return TABLE_TYPE;
    }

    @Override
    public Object getItem() {
        return table;
    }

    public class TableHolder{

        TextView name;
    }
}
