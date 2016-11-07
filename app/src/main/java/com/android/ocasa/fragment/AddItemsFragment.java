package com.android.ocasa.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.ocasa.adapter.AddItemsReceiptAdapter;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.event.ReceiptItemAddEvent;
import com.android.ocasa.loader.ActionRecordTaskLoader;
import com.android.ocasa.pickup.util.PickupItemConfirmationDialog;
import com.android.ocasa.receipt.edit.OnItemChangeListener;
import com.android.ocasa.viewmodel.TableViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Ignacio Oviedo on 21/03/16.
 */
public class AddItemsFragment extends RecyclerListFragment implements LoaderManager.LoaderCallbacks<TableViewModel>,
    PickupItemConfirmationDialog.OnConfirmationListener{

    static final String ARG_RECEIPT_ID = "receipt_id";
    static final String ARG_SEARCH_QUERY = "search_query";
    static final String ARG_EXCLUDE_IDS = "exclude_ids";

    private OnItemChangeListener callback;

    public static AddItemsFragment newInstance(long receiptId, String query, long[] exludeIds) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);
        args.putString(ARG_SEARCH_QUERY, query);
        args.putLongArray(ARG_EXCLUDE_IDS, exludeIds);

        AddItemsFragment fragment = new AddItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = (OnItemChangeListener) getParentFragment();

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView list = getRecyclerView();
        list.setBackgroundColor(Color.parseColor("#9E9E9E"));
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new ActionRecordTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID),
                args.getString(ARG_SEARCH_QUERY),
                args.getLongArray(ARG_EXCLUDE_IDS));
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        if(data == null)
            return;

        callback.onShowSearchResults();

        if(data.getCells().isEmpty()){
            callback.onItemNotFound(getArguments()
                    .getString(ARG_SEARCH_QUERY));
            getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        if(data.getCells().size() == 1){
            callback.onItemAdded(data.getCells().get(0));
            getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        setListShown(true);
        setAdapter(new AddItemsReceiptAdapter(data.getCells()));
    }

    @Override
    public void onLoaderReset(Loader<TableViewModel> loader) {

    }

    @Subscribe
    public void onItemClick(ReceiptItemAddEvent event){
        AddItemsReceiptAdapter adapter = (AddItemsReceiptAdapter) getAdapter();
        adapter.removeItem(event.getPosition());

//        callback.onItemAdded(event.getRecordId());

        if(adapter.getItemCount() == 0){
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void search(String query){

        Loader loader = getLoaderManager().getLoader(0);
        if(loader != null)
            loader.cancelLoad();

        getArguments().putString(ARG_SEARCH_QUERY, query);

        getLoaderManager().restartLoader(0, getArguments(), this);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onAdd(String code) {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
