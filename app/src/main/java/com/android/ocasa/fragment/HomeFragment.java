package com.android.ocasa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.ocasa.R;
import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.adapter.RecordAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.fragment.ListFragment;
import com.android.ocasa.loader.TableTaskLoader;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.UserService;
import com.android.ocasa.sync.SyncService;

import java.util.List;

/**
 * Created by ignacio on 11/01/16.
 */
public class HomeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Record>>{

    static final String ARG_TABLE_ID = "table_id";

    private RecordSyncReceiver receiver;
    private IntentFilter filter;

    public static HomeFragment newInstance(String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);

        syncTable();
        syncRecords();
    }

    private void syncTable(){
        Intent intent = new Intent(getActivity(), SyncService.class);
        intent.putExtra(SyncService.EXTRA_SYNC, SyncService.TABLE_SYNC);
        intent.putExtra(SyncService.EXTRA_ID, getArguments().getString(ARG_TABLE_ID));

        getActivity().startService(intent);
    }

    private void syncRecords(){
        Intent intent = new Intent(getActivity(), SyncService.class);
        intent.putExtra(SyncService.EXTRA_SYNC, SyncService.RECORD_SYNC);
        intent.putExtra(SyncService.EXTRA_ID, getArguments().getString(ARG_TABLE_ID));

        getActivity().startService(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView list =  getRecyclerView();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        receiver = new RecordSyncReceiver();
        filter = new IntentFilter(UserService.USER_LOGIN_FINISHED_ACTION);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public Loader<List<Record>> onCreateLoader(int id, Bundle args) {
        return new TableTaskLoader(getActivity(), args.getString(ARG_TABLE_ID));
    }

    @Override
    public void onLoadFinished(Loader<List<Record>> loader, List<Record> data) {

        setListShown(true);

        if(data!= null)
            getRecyclerView().setAdapter(new RecordAdapter(data));
    }

    @Override
    public void onLoaderReset(Loader<List<Record>> loader) {

    }

    @Override
    public void onListItemClick(View v, int position, long id) {
        super.onListItemClick(v, position, id);

        Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
        intent.putExtra(DetailRecordActivity.EXTRA_RECORD_ID, id);
        ((BaseActivity) getActivity()).startNewActivity(intent);
    }

    public class RecordSyncReceiver extends BroadcastReceiver {

        public RecordSyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            getLoaderManager().restartLoader(0, getArguments(), HomeFragment.this);
        }
    }
}
