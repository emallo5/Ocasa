package com.android.ocasa.receipt.item.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.ocasa.R;
import com.android.ocasa.core.TableFragment;
import com.android.ocasa.core.TablePresenter;
import com.android.ocasa.receipt.edit.OnItemChangeListener;
import com.android.ocasa.receipt.item.update.UpdateReceiptItemActivity;
import com.android.ocasa.adapter.ReceiptItemsAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.event.ReceiptItemDeleteEvent;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ReceiptItemsFragment extends TableFragment{

    static final String ARG_RECEIPT_ID = "receipt_id";

    private RecyclerView list;
    private ProgressBar progress;

    private OnItemChangeListener callback;

    public static ReceiptItemsFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        ReceiptItemsFragment fragment = new ReceiptItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnItemChangeListener) getParentFragment();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public Loader<TablePresenter> getPresenterLoader() {
        return new ReceiptItemsLoader(getActivity());
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

        progress = (ProgressBar) view.findViewById(R.id.progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ReceiptItemsPresenter)getPresenter()).load(getArguments().getLong(ARG_RECEIPT_ID));
    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){
//        Intent intent = new Intent(getActivity(), UpdateReceiptItemActivity.class);
//        intent.putExtra(UpdateReceiptItemActivity.EXTRA_RECORD_ID, event.getRecordId());
//        intent.putExtra(UpdateReceiptItemActivity.EXTRA_ACTION_ID, getArguments().getLong(ARG_RECEIPT_ID));
//        ((BaseActivity)getActivity()).startNewActivity(intent);
    }

    @Subscribe
    public void onItemDeleteClick(ReceiptItemDeleteEvent event){
        callback.onItemRemoved(getPresenter().getItemAtPosition(event.getPosition()));

        ReceiptItemsAdapter adapter = (ReceiptItemsAdapter) list.getAdapter();
        adapter.deleteItem(event.getPosition());
    }

    public void addItem(CellViewModel record){
        if(list.getAdapter() == null){
            record.setNumber(1);
            list.setAdapter(new ReceiptItemsAdapter(record));
            return;
        }

        list.scrollToPosition(0);

        ReceiptItemsAdapter adapter = (ReceiptItemsAdapter) list.getAdapter();
        adapter.addItem(record);
    }

    public void addItems(List<CellViewModel> record){
        if(list.getAdapter() == null){
            list.setAdapter(new ReceiptItemsAdapter(record));
            progress.setVisibility(View.GONE);
            return;
        }

        list.scrollToPosition(0);

        ReceiptItemsAdapter adapter = (ReceiptItemsAdapter) list.getAdapter();
        adapter.addItems(record);
    }

    @Override
    public void onTableLoadSuccess(TableViewModel table) {

        progress.setVisibility(View.GONE);

        list.setAdapter(new ReceiptItemsAdapter(table.getCells()));
        list.scrollToPosition(0);

        long[] recordIds = new long[table.getCells().size()];

        for (int index = 0; index < table.getCells().size(); index++){
            CellViewModel cell = table.getCells().get(index);
            recordIds[index] = cell.getId();
        }

        callback.onPreviousItemsLoaded(recordIds);
    }


}
