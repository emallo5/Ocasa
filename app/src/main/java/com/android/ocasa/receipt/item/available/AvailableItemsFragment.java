package com.android.ocasa.receipt.item.available;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.android.ocasa.OcasaApplication;
import com.android.ocasa.adapter.AvailableItemsAdapter;
import com.android.ocasa.cache.dao.ActionDAO;
import com.android.ocasa.cache.dao.ReceiptDAO;
import com.android.ocasa.cache.dao.ReceiptItemDAO;
import com.android.ocasa.core.TableFragment;
import com.android.ocasa.core.TablePresenter;
import com.android.ocasa.event.ReceiptItemAddEvent;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.receipt.edit.EditReceiptFragment;
import com.android.ocasa.receipt.edit.OnItemChangeListener;
import com.android.ocasa.util.ReceiptCounterHelper;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

public class AvailableItemsFragment extends TableFragment {

    static final String ARG_RECEIPT_ID = "receipt_id";

    private OnItemChangeListener itemCallback;

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
        if(getAdapter() == null) {
            ((AvailableItemsPresenter) getPresenter()).load(getArguments().getLong(ARG_RECEIPT_ID));
            ((OcasaApplication) getActivity().getApplicationContext()).availableItemsLoading = true;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pending = getAdapter().getItemCount();
                    int total = ReceiptCounterHelper.getInstance().getCompletedItemsCount() + ReceiptCounterHelper.getInstance().getCompletedSyncItemsCount() + pending;
                    ((EditReceiptFragment) getParentFragment()).setTitleFromChild(total, pending);
                }
            }, 1000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((OcasaApplication) getActivity().getApplicationContext()).availableItemsLoading = false;
    }

    @Override
    public Loader<TablePresenter> getPresenterLoader() {
        return new AvailableItemsLoader(getActivity());
    }

    @Override
    public void onTableLoadSuccess(TableViewModel table) {
        if(table == null)
            return;

        if (!table.getOrderBy().isEmpty()) orderRecords(table.getCells(), table.getOrderBy());

        if (getAdapter() == null) {
            setListShown(true);
            setAdapter(new AvailableItemsAdapter(table.getCells()));
        }
        else ((AvailableItemsAdapter)getAdapter()).refreshItems(table.getCells());

        getRecyclerView().setBackgroundColor(Color.WHITE);

        int pending = table.getCells().size();
        int total = ReceiptCounterHelper.getInstance().getCompletedItemsCount() + ReceiptCounterHelper.getInstance().getCompletedSyncItemsCount() + pending;
        ((EditReceiptFragment) getParentFragment()).setTitleFromChild(total, pending);
    }

    @Subscribe
    public void onItemClick(ReceiptItemAddEvent event) {
        itemCallback.onItemAdded(((AvailableItemsAdapter) getAdapter()).getItem(event.getPosition()));
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

    public void filterItems(String filter) {
        int results = 2;
        if (getAdapter() != null)
            results = ((AvailableItemsAdapter) getAdapter()).filter(filter);

        if (results == 0) {
            itemCallback.onCreateItem(filter);
        } else if (results == 1)
            itemCallback.onItemAdded(((AvailableItemsAdapter) getAdapter()).getItem(0));
    }

    public void orderRecords(List<CellViewModel> cells, final List<String> orderBy) {
        Collections.sort(cells, new Comparator<CellViewModel>() {
            @Override
            public int compare(CellViewModel o1, CellViewModel o2) {
                for (String comparing : orderBy) {
                    try {
                        int result = o1.getColumnValue(comparing).compareTo(o2.getColumnValue(comparing));
                        if (result == 0) continue;
                        else return result;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
                return 0;
            }
        });
    }
}
