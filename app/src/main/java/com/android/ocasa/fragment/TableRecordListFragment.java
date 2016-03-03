package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.ocasa.R;
import com.android.ocasa.adapter.RecordAdapter;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.loader.TableTaskLoader;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 15/02/16.
 */
public abstract class TableRecordListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Table>{

    static final String ARG_TABLE_ID = "table_id";
    static final String ARG_SEARCH_QUERY = "search_query";

    private boolean wasPaused = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(wasPaused){
            reloadData();
            wasPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        wasPaused = true;
    }

    public void reloadData(){
        getLoaderManager().restartLoader(0, getArguments(), this);
    }

    @Override
    public Loader<Table> onCreateLoader(int id, Bundle args) {
        return new TableTaskLoader(getActivity(), args.getString(ARG_TABLE_ID),
                args.getString(ARG_SEARCH_QUERY));
    }

    @Override
    public void onLoadFinished(Loader<Table> loader, Table data) {

        if(getListAdapter() == null)
            setListShown(true);

        setListAdapter(new RecordAdapter(new ArrayList<Record>(data.getRecords())));
    }

    @Override
    public void onLoaderReset(Loader<Table> loader) {

    }

}
