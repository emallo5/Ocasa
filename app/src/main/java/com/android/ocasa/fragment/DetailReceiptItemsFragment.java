package com.android.ocasa.fragment;

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
import com.android.ocasa.activity.DetailReceiptItemActivity;
import com.android.ocasa.adapter.ReceiptItemsDetailAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.loader.ReceiptRecordsTaskLoader;
import com.android.ocasa.viewmodel.CellViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class DetailReceiptItemsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<CellViewModel>>{

    static final String ARG_RECEIPT_ID = "receipt_id";

    private RecyclerView list;

    public static DetailReceiptItemsFragment newInstance(long receiptId) {
        
        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        DetailReceiptItemsFragment fragment = new DetailReceiptItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_items, container, false);
    }

   @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       list = (RecyclerView) view.findViewById(android.R.id.list);
       list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
       list.setHasFixedSize(true);
       list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
               .showLastDivider().build());

        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setVisibility(View.GONE);
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
    public void onItemClick(ReceiptItemEvent event){
        Intent intent = new Intent(getActivity(), DetailReceiptItemActivity.class);
        intent.putExtra(DetailReceiptItemActivity.EXTRA_RECORD_ID, event.getRecordId());
        intent.putExtra(DetailReceiptItemActivity.EXTRA_RECEIPT_ID, getArguments().getLong(ARG_RECEIPT_ID));
        ((BaseActivity)getActivity()).startNewActivity(intent);
    }

    @Override
    public Loader<List<CellViewModel>> onCreateLoader(int id, Bundle args) {
        return new ReceiptRecordsTaskLoader(getContext(), args.getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void onLoadFinished(Loader<List<CellViewModel>> loader, List<CellViewModel> data) {
        if(!data.isEmpty()){
            list.setAdapter(new ReceiptItemsDetailAdapter(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<CellViewModel>> loader) {

    }
}
