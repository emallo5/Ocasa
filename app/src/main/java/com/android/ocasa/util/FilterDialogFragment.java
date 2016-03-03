package com.android.ocasa.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.ocasa.R;
import com.android.ocasa.loader.ColumnTaskLoader;
import com.android.ocasa.model.Column;
import com.android.ocasa.widget.FilterTextView;

import java.util.List;

/**
 * Created by ignacio on 15/02/16.
 */
public class FilterDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<List<Column>>{

    static final String ARG_TABLE_ID = "table_id";

    private LinearLayout container;

    private ApplyFilterListener callback;

    public interface ApplyFilterListener{
        public void apply(Filter filter);
    }

    public static FilterDialogFragment newInstance(String tableId) {
        
        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);
        
        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (ApplyFilterListener) activity;
        }catch (ClassCastException e){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getDelegate().getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_clear_mtrl_alpha);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = (LinearLayout) view.findViewById(R.id.container);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();

                return true;
            case R.id.apply:
                Filter filter = new Filter();

                filter.addSimpleCondition("test");
                filter.addSimpleCondition("description");

                callback.apply(filter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Column>> onCreateLoader(int id, Bundle args) {
        return new ColumnTaskLoader(getActivity(), args.getString(ARG_TABLE_ID));
    }

    @Override
    public void onLoadFinished(Loader<List<Column>> loader, List<Column> data) {

        if(!data.isEmpty()){
            fillFilters(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Column>> loader) {

    }

    private void fillFilters(List<Column> columns){

        for (Column column : columns){

            FilterTextView filter = new FilterTextView(getActivity());
            filter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            filter.setLabel(column.getName());

            container.addView(filter);
        }
    }
}
