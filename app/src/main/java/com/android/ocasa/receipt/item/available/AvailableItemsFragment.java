package com.android.ocasa.receipt.item.available;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.android.ocasa.adapter.AvailableItemsAdapter;
import com.android.ocasa.cache.dao.ReceiptItemDAO;
import com.android.ocasa.core.TableFragment;
import com.android.ocasa.core.TablePresenter;
import com.android.ocasa.event.ReceiptItemAddEvent;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.receipt.edit.OnItemChangeListener;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by ignacio on 19/05/16.
 */
public class AvailableItemsFragment extends TableFragment {

    static final String ARG_RECEIPT_ID = "receipt_id";

    private OnItemChangeListener itemCallback;
    private boolean loadingData = false;

    public static AvailableItemsFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        AvailableItemsFragment fragment = new AvailableItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        itemCallback = (OnItemChangeListener) getParentFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getAction().setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getAdapter() == null && !loadingData) {
//            ((EditReceiptFragment) getParentFragment()).showProgressCustom("Cargando...");
            ((AvailableItemsPresenter) getPresenter()).load(getArguments().getLong(ARG_RECEIPT_ID));
            loadingData = true;
        }
    }

    @Override
    public Loader<TablePresenter> getPresenterLoader() {
        return new AvailableItemsLoader(getActivity());
    }

    @Override
    public void onTableLoadSuccess(TableViewModel table) {
        if(table == null)
            return;

        loadingData = false;

        if(getAdapter() == null) {
            setListShown(true);
            setAdapter(new AvailableItemsAdapter(table.getCells()));
        }else{
            ((AvailableItemsAdapter)getAdapter()).refreshItems(table.getCells());
        }

        getRecyclerView().setBackgroundColor(Color.WHITE);

//        ((EditReceiptFragment) getParentFragment()).hideProgress();
    }

    @Subscribe
    public void onItemClick(ReceiptItemAddEvent event) {
        itemCallback.onItemAdded(getPresenter().getItemAtPosition(event.getPosition()));
    }

    public void removeitem(long id) {
        AvailableItemsAdapter adapter = (AvailableItemsAdapter) getAdapter();

        if (adapter == null) return;

        adapter.deleteItem(id);
        adapter.notifyDataSetChanged();
    }

    public void addItem(CellViewModel item){
        ((AvailableItemsAdapter)getAdapter()).addItem(item);
    }
}
