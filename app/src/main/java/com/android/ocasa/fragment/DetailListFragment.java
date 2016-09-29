package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ocasa.adapter.DetailAdapter;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.loader.DetailListTaskLoader;
import com.android.ocasa.model.Record;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by ignacio on 25/02/16.
 */
public class DetailListFragment extends RecyclerListFragment implements LoaderManager.LoaderCallbacks<List<Record>> {

    static final String ARG_TITLE = "title";
    static final String ARG_TABLE_ID = "table_id";
    static final String ARG_COLUMN_ID = "column_id";
    static final String ARG_VALUE = "value";

    public static DetailListFragment newInstance(String title, String tableId, String columnId, String value) {
        
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TABLE_ID, tableId);
        args.putString(ARG_COLUMN_ID, columnId);
        args.putString(ARG_VALUE, value);
        
        DetailListFragment fragment = new DetailListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getArguments().getString(ARG_TITLE));

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView list =  getRecyclerView();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).showLastDivider().build());

    }

    @Override
    public Loader<List<Record>> onCreateLoader(int id, Bundle args) {
        return new DetailListTaskLoader(getContext(),
                args.getString(ARG_TABLE_ID),
                args.getString(ARG_COLUMN_ID),
                args.getString(ARG_VALUE));
    }

    @Override
    public void onLoadFinished(Loader<List<Record>> loader, List<Record> data) {

        setListShown(true);

        if(!data.isEmpty())
            fillDetailList(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Record>> loader) {

    }

    private void fillDetailList(List<Record> records){

       getRecyclerView().setAdapter(new DetailAdapter(records));
    }

}
