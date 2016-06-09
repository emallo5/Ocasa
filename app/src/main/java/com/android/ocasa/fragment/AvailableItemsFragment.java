package com.android.ocasa.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.loader.ActionRecordTaskLoader;
import com.android.ocasa.viewmodel.TableViewModel;

/**
 * Created by ignacio on 19/05/16.
 */
public class AvailableItemsFragment extends TableRecordListFragment {

    static final String ARG_RECEIPT_ID = "receipt_id";

    private OnTableLoad callback;

    private AddItemsFragment.OnItemAddedListener itemCallback;

    public interface OnTableLoad{
        void onLoad(String tableName);
    }

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

        callback = (OnTableLoad) getParentFragment();
        itemCallback = (AddItemsFragment.OnItemAddedListener) getParentFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getRecyclerView().setBackgroundColor(Color.WHITE);
    }

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new ActionRecordTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID),
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        super.onLoadFinished(loader, data);

        callback.onLoad(data.getName());
    }

    @Override
    public void onItemClick(ReceiptItemEvent event) {
        //super.onItemClick(event);
        itemCallback.onItemAdded(event.getRecordId());
    }
}
