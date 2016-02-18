package com.android.ocasa.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.ocasa.R;
import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.model.Record;
import com.android.ocasa.service.RecordService;
import com.android.ocasa.sync.SyncService;

import java.util.List;

/**
 * Created by ignacio on 11/01/16.
 */
public class HomeFragment extends TableRecordListFragment implements LoaderManager.LoaderCallbacks<List<Record>>, SearchView.OnQueryTextListener{

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

        setHasOptionsMenu(true);

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

        ListView list =  getListView();
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.menu_filter, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
        intent.putExtra(DetailRecordActivity.EXTRA_RECORD_ID, id);
        ((BaseActivity) getActivity()).startNewActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_table_list, menu);

        MenuItem menuItem = menu.findItem(R.id.filter);
        SearchView searchView = (SearchView) menuItem.getActionView();
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
