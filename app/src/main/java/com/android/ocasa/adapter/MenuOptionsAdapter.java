package com.android.ocasa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.adapter.AdapterItem;
import com.android.ocasa.core.adapter.MultiItemAdapter;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Table;

import java.util.List;

/**
 * Created by ignacio on 04/02/16.
 */
public class MenuOptionsAdapter extends MultiItemAdapter {

    static final int CATEGORY_TYPE = 1;
    static final int TABLE_TYPE = 2;

    public MenuOptionsAdapter(List<Category> categories){
        super();

        addAdapter(new CategoryAdapter(), CATEGORY_TYPE);
        addAdapter(new TableAdapter(), TABLE_TYPE);

        for (Category category : categories){
            addItem(new AdapterItem(CATEGORY_TYPE, category));

            for (Table table : category.getTables()){
                addItem(new AdapterItem(TABLE_TYPE, table));
            }
        }

        setHasStableIds(true);
    }

}
