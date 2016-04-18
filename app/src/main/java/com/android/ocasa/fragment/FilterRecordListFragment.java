package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.ocasa.R;

/**
 * Ignacio Oviedo on 07/04/16.
 */
public abstract class FilterRecordListFragment extends TableRecordListFragment implements SearchView.OnQueryTextListener{

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_table_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.filter);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//      /*  switch (item.getItemId()){
//            case R.id.filter:
//                Intent intent = new Intent(getActivity(), FilterActivity.class);
//                intent.putExtra("table_id", getArguments().getString(ARG_TABLE_ID));
//
//                getActivity().startActivityForResult(intent, 1200);
//                getActivity().overridePendingTransition(R.anim.slide_up_dialog, R.anim.no_change);
//                return true;
//        }*/
//
//        return super.onOptionsItemSelected(item);
//    }

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
