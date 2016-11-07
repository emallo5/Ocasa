package com.android.ocasa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ocasa.R;
import com.android.ocasa.adapter.HistoricalAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.cache.dao.HistoryDAO;
import com.android.ocasa.event.ReceiptHistoryClickEvent;
import com.android.ocasa.loader.FieldTaskLoader;
import com.android.ocasa.model.History;
import com.android.ocasa.receipt.detail.DetailReceiptActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ignacio on 11/02/16.
 */
public class FieldHistoricalFragment extends RecyclerListFragment implements LoaderManager.LoaderCallbacks<List<History>>{

    static final String ARG_RECORD_ID = "record_id";
    static final String ARG_COLUMN_ID = "column_id";

    public static FieldHistoricalFragment newInstance(long recordId, String columnId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECORD_ID, recordId);
        args.putString(ARG_COLUMN_ID, columnId);

        FieldHistoricalFragment fragment = new FieldHistoricalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.historical_title));

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onItemClick(ReceiptHistoryClickEvent event){

        History history = new HistoryDAO(getActivity()).findById(event.getHistoryId());

        Intent intent = new Intent(getActivity(), DetailReceiptActivity.class);
        intent.putExtra(DetailReceiptActivity.EXTRA_RECEIPT_ID, history.getReceipt().getId());
        ((BaseActivity) getActivity()).startNewActivity(intent);
    }

    @Override
    public Loader<List<History>> onCreateLoader(int id, Bundle args) {
        return new FieldTaskLoader(getActivity(), args.getLong(ARG_RECORD_ID), args.getString(ARG_COLUMN_ID));
    }

    @Override
    public void onLoadFinished(Loader<List<History>> loader, List<History> data) {

        setListShown(true);

        if(!data.isEmpty()){
            getRecyclerView().setAdapter(new HistoricalAdapter(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<History>> loader) {

    }
}
