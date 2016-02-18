package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ocasa.adapter.HistoricalAdapter;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.loader.FieldTaskLoader;
import com.android.ocasa.model.Field;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by ignacio on 11/02/16.
 */
public class FieldHistoricalFragmentRecycler extends RecyclerListFragment implements LoaderManager.LoaderCallbacks<Field>{

    static final String ARG_FIELD_ID = "field_id";

    public static FieldHistoricalFragmentRecycler newInstance(int fieldId) {

        Bundle args = new Bundle();
        args.putInt(ARG_FIELD_ID, fieldId);

        FieldHistoricalFragmentRecycler fragment = new FieldHistoricalFragmentRecycler();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public Loader<Field> onCreateLoader(int id, Bundle args) {
        return new FieldTaskLoader(getActivity(), args.getInt(ARG_FIELD_ID));
    }

    @Override
    public void onLoadFinished(Loader<Field> loader, Field data) {

        setListShown(true);

        if(!data.getHistorical().isEmpty()){
            getRecyclerView().setAdapter(new HistoricalAdapter(new ArrayList<>(data.getHistorical())));
        }

    }

    @Override
    public void onLoaderReset(Loader<Field> loader) {

    }
}
