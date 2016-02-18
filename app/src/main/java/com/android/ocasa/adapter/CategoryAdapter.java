package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.AdapterItem;
import com.android.ocasa.core.adapter.DelegateAdapter;
import com.android.ocasa.model.Category;

/**
 * Created by ignacio on 10/02/16.
 */
public class CategoryAdapter implements DelegateAdapter{


    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, AdapterItem item) {

        CategoryHolder categoryHolder = (CategoryHolder) holder;

        Category app = (Category) item.getData();

        categoryHolder.name.setText(app.getName().toUpperCase());
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{

        TextView name;

        public CategoryHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
        }
    }


}
