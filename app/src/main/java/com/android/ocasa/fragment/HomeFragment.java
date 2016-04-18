package com.android.ocasa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;

import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.sync.SyncService;
import com.android.ocasa.viewmodel.TableViewModel;

/**
 * Ignaco Oviedo on 11/01/16.
 */
public class HomeFragment extends FilterRecordListFragment{

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

        receiver = new RecordSyncReceiver();
        filter = new IntentFilter();
        filter.addAction(RecordService.RECORD_SYNC_FINISHED_ACTION);
        filter.addAction(RecordService.RECORD_SYNC_ERROR_ACTION);
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
    public void onItemClick(ReceiptItemEvent event) {
        super.onItemClick(event);

        Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
        intent.putExtra(DetailRecordActivity.EXTRA_RECORD_ID, event.getRecordId());
        ((BaseActivity) getActivity()).startNewActivity(intent);
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        super.onLoadFinished(loader, data);

        if(data != null)
            ((BaseActivity)getActivity()).getDelegate().getSupportActionBar().setTitle(data.getName());
    }

    public class RecordSyncReceiver extends BroadcastReceiver {

        public RecordSyncReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(RecordService.RECORD_SYNC_FINISHED_ACTION))
                getLoaderManager().restartLoader(0, getArguments(), HomeFragment.this);
            else if(intent.getAction().equalsIgnoreCase(RecordService.RECORD_SYNC_ERROR_ACTION)){
                setListShown(true);
            }
        }
    }
}
