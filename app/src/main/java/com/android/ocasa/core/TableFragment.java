package com.android.ocasa.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ocasa.adapter.RecordAdapter;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.viewmodel.TableViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ignacio on 18/07/16.
 */
public class TableFragment extends RecyclerListActionMvpFragment<TableView, TablePresenter> implements TableView{

    public static final String ARG_LAYOUT_ID = "layout_id";
    public static final String ARG_SEARCH_QUERY = "search_query";

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
    public void onResume() {
        super.onResume();
        getPresenter().load(getArguments().getString(ARG_LAYOUT_ID));
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

    @Override
    public TableView getMvpView() {
        return this;
    }

    @Override
    public Loader<TablePresenter> getPresenterLoader() {
        return new TableLoader(getActivity());
    }

    @Override
    public void onTableLoadSuccess(TableViewModel table) {

        if(table == null)
            return;

        if(getAdapter() == null) {
            setListShown(true);
            setAdapter(new RecordAdapter(table));
        }else{
            ((RecordAdapter)getAdapter()).refreshItems(table);
        }
    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){

    }

    public void reloadData(){
        getPresenter().load(getArguments().getString(ARG_LAYOUT_ID), getArguments().getString(ARG_SEARCH_QUERY, null));
    }

}
