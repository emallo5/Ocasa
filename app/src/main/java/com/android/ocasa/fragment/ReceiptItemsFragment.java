package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.activity.AddItemsActivity;
import com.android.ocasa.activity.UpdateReceiptItemActivity;
import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.adapter.ReceiptItemsAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.event.ReceiptItemDeleteEvent;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.loader.RecordsTaskLoaderTest;
import com.android.ocasa.viewmodel.CellViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.commons.lang3.ArrayUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ReceiptItemsFragment extends BaseActionFragment{

    static final int REQUEST_ADD_ITEMS = 1000;

    public interface OnAddItemsListener{
        void onItemsCountChange(int count);
    }

    private FloatingActionButton add;
    private RecyclerView list;

    private long[] recordIds;

    private boolean wasPaused = false;

    private RecordsLoaderCallback recordsCallback = new RecordsLoaderCallback();

    private OnAddItemsListener callback;

    public static ReceiptItemsFragment newInstance(String actionId) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        ReceiptItemsFragment fragment = new ReceiptItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnAddItemsListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list.setHasFixedSize(true);
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .showLastDivider().build());

        add = (FloatingActionButton) view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddItemsActivity.class);
                intent.putExtra(AddItemsActivity.EXTRA_ACTION_ID, action.getId());
                intent.putExtra(AddItemsActivity.EXTRA_EXCLUDE_IDS, recordIds);
                startActivityForResult(intent, 1000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ADD_ITEMS
                && resultCode == Activity.RESULT_OK){

            Bundle extras = data.getExtras();

            recordIds = ArrayUtils.addAll(recordIds, extras.getLongArray(DetailRecordActivity.EXTRA_RECORDS_ID));

            callback.onItemsCountChange(recordIds.length);

            if(getLoaderManager().getLoader(1) == null) {
                getLoaderManager().initLoader(1, data.getExtras(), new RecordsLoaderCallback());
                return;
            }

            extras.putLongArray(DetailRecordActivity.EXTRA_RECORDS_ID, recordIds);
            getLoaderManager().restartLoader(1, extras, recordsCallback);
        }
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
            Bundle bundle = new Bundle();
            bundle.putLongArray(DetailRecordActivity.EXTRA_RECORDS_ID, recordIds);
            getLoaderManager().restartLoader(1, bundle, recordsCallback);
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
        Intent intent = new Intent(getActivity(), UpdateReceiptItemActivity.class);
        intent.putExtra(UpdateReceiptItemActivity.EXTRA_RECORD_ID, event.getRecordId());
        intent.putExtra(UpdateReceiptItemActivity.EXTRA_AVAILABLE_COLUMNS, action.getDetailsComlumIds());
        ((BaseActivity)getActivity()).startNewActivity(intent);
    }

    @Subscribe
    public void onItemDeleteClick(ReceiptItemDeleteEvent event){
        recordIds = ArrayUtils.remove(recordIds, event.getPosition());
        ((ReceiptItemsAdapter) list.getAdapter()).deleteItem(event.getPosition());
        callback.onItemsCountChange(recordIds.length);
    }

    public long[] getRecordIds(){
        return recordIds;
    }

    public class RecordsLoaderCallback implements LoaderManager.LoaderCallbacks<List<CellViewModel>>{

        @Override
        public Loader<List<CellViewModel>> onCreateLoader(int id, Bundle args) {
            return new RecordsTaskLoaderTest(getActivity(), args.getLongArray(DetailRecordActivity.EXTRA_RECORDS_ID));
        }

        @Override
        public void onLoadFinished(Loader<List<CellViewModel>> loader, List<CellViewModel> data) {

            if(!data.isEmpty())
                list.setAdapter(new ReceiptItemsAdapter(data));
        }

        @Override
        public void onLoaderReset(Loader<List<CellViewModel>> loader) {

        }
    }
}
