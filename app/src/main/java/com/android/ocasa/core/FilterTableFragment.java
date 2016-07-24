package com.android.ocasa.core;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by ignacio on 21/07/16.
 */
public class FilterTableFragment extends TableFragment implements SearchView.OnQueryTextListener{

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(com.android.ocasa.R.menu.menu_table_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(com.android.ocasa.R.id.filter);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    public void submitQuery(String query){
        searchView.setQuery(query, true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        getArguments().putString(ARG_SEARCH_QUERY, query);

        reloadData();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getArguments().putString(ARG_SEARCH_QUERY, newText);

        reloadData();

        return true;
    }
}
