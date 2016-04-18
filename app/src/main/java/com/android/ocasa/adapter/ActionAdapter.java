package com.android.ocasa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.DelegateListAdapter;
import com.android.ocasa.model.Action;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ActionAdapter implements DelegateListAdapter {

    private Action action;

    public ActionAdapter(Action action) {
        this.action = action;
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

        holder.name.setText(action.getName());

        return convertView;
    }

    @Override
    public int getViewType() {
        return MenuOptionsAdapter.ACTION_TYPE;
    }

    @Override
    public Object getItem() {
        return action;
    }

    public class TableHolder{

        TextView name;
    }
}
