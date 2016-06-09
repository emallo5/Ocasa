package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ocasa.adapter.RecordAdapterTest;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.loader.TableTaskLoader;
import com.android.ocasa.viewmodel.TableViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Ignacio Oviedo on 15/02/16.
 */
public abstract class TableRecordListFragment extends RecyclerListFragment implements LoaderManager.LoaderCallbacks<TableViewModel>{

    public static final String ARG_TABLE_ID = "table_id";
    public static final String ARG_SEARCH_QUERY = "search_query";

    private boolean wasPaused = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView list = getRecyclerView();
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setHasFixedSize(true);
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .showLastDivider().build());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){

    }

    public void reloadData(){
        getLoaderManager().restartLoader(0, getArguments(), this);
    }

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new TableTaskLoader(getActivity(), args.getString(ARG_TABLE_ID),
                args.getString(ARG_SEARCH_QUERY));
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {

        if(data == null)
            return;

        if(getAdapter() == null) {
            setListShown(true);
            setAdapter(new RecordAdapterTest(data.getCells()));
        }else{
            ((RecordAdapterTest)getAdapter()).refreshItems(data.getCells());
        }
    }

    @Override
    public void onLoaderReset(Loader<TableViewModel> loader) {

    }

}
