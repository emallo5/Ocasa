package com.android.ocasa.receipt.item.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ocasa.R;
import com.android.ocasa.receipt.item.update.UpdateReceiptItemActivity;
import com.android.ocasa.adapter.ReceiptItemsAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.event.ReceiptItemDeleteEvent;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.viewmodel.CellViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ReceiptItemsFragment extends BaseFragment{

    static final String ARG_RECEIPT_ID = "receipt_id";

    public interface OnAddItemsListener{
        void onItemRemoved(long recordId);
    }

    private RecyclerView list;

    private long[] recordIds;

    private boolean wasPaused = false;

    private OnAddItemsListener callback;

    public static ReceiptItemsFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);
//
        ReceiptItemsFragment fragment = new ReceiptItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnAddItemsListener) getParentFragment();
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

//        add = (FloatingActionButton) view.findViewById(R.id.add);
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), AddItemsActivity.class);
//                intent.putExtra(AddItemsActivity.EXTRA_ACTION_ID, action.getId());
//                intent.putExtra(AddItemsActivity.EXTRA_EXCLUDE_IDS, recordIds);
//                startActivityForResult(intent, 1000);
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if(wasPaused){
//            Bundle bundle = new Bundle();
//            bundle.putLongArray(DetailRecordActivity.EXTRA_RECORDS_ID, recordIds);
//            getLoaderManager().restartLoader(1, bundle, recordsCallback);
//            wasPaused = false;
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        wasPaused = true;
//    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){
        Intent intent = new Intent(getActivity(), UpdateReceiptItemActivity.class);
        intent.putExtra(UpdateReceiptItemActivity.EXTRA_RECORD_ID, event.getRecordId());
        intent.putExtra(UpdateReceiptItemActivity.EXTRA_ACTION_ID, getArguments().getLong(ARG_RECEIPT_ID));
        ((BaseActivity)getActivity()).startNewActivity(intent);
    }

    @Subscribe
    public void onItemDeleteClick(ReceiptItemDeleteEvent event){
        ReceiptItemsAdapter adapter = (ReceiptItemsAdapter) list.getAdapter();
        adapter.deleteItem(event.getPosition());
        callback.onItemRemoved(event.getId());
//        recordIds = ArrayUtils.remove(recordIds, event.getPosition());
//        ((ReceiptItemsAdapter) list.getAdapter()).deleteItem(event.getPosition());
//        callback.onItemsCountChange(recordIds.length);
    }

    public long[] getRecordIds(){
        return recordIds;
    }

    public void addItem(CellViewModel record){
        if(list.getAdapter() == null){
            record.setNumber(1);
            list.setAdapter(new ReceiptItemsAdapter(record));
            return;
        }

        ReceiptItemsAdapter adapter = (ReceiptItemsAdapter) list.getAdapter();
        adapter.addItem(record);
        list.scrollToPosition(0);
    }

    public void addItems(List<CellViewModel> record){
        if(list.getAdapter() == null){
            list.setAdapter(new ReceiptItemsAdapter(record));
            return;
        }

        list.scrollToPosition(0);

        ReceiptItemsAdapter adapter = (ReceiptItemsAdapter) list.getAdapter();
        adapter.addItems(record);
    }
}
