package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.AdapterItem;
import com.android.ocasa.core.adapter.DelegateAdapter;
import com.android.ocasa.core.adapter.DelegateListAdapter;
import com.android.ocasa.model.Category;

/**
 * Created by ignacio on 10/02/16.
 */
public class CategoryAdapter implements DelegateListAdapter{


    static final int CATEGORY_TYPE = 1;

    private Category category;

    public CategoryAdapter(Category category) {
        this.category = category;
    }

    @Override
    public View createView(View convertView, ViewGroup parent, int position) {

        CategoryHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);

            holder = new CategoryHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        }else{
            holder = (CategoryHolder) convertView.getTag();
        }

        holder.name.setText(category.getName().toUpperCase());

        return convertView;
    }

    @Override
    public int getViewType() {
        return CATEGORY_TYPE;
    }

    @Override
    public Object getItem() {
        return category;
    }

    public class CategoryHolder{

        TextView name;
    }


}
