package com.android.ocasa.pickup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.android.ocasa.adapter.ReceiptItemsAdapter;
import com.android.ocasa.event.ReceiptItemDeleteEvent;
import com.android.ocasa.fragment.TableRecordListFragment;
import com.android.ocasa.pickup.loader.PickupFoundItemsTaskLoader;
import com.android.ocasa.viewmodel.TableViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 25/04/16.
 */
public class PickupFoundItemsFragment extends TableRecordListFragment implements PickupFragment.PickUpItemsCallback{

    static final String ARG_ITEM_IDS = "item_ids";

    private OnLoadItemsCallback callback;

    public static PickupFoundItemsFragment newInstance(String tableId, List<String> itemIds) {
        
        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);
        args.putStringArrayList(ARG_ITEM_IDS, (ArrayList<String>) itemIds);
        
        PickupFoundItemsFragment fragment = new PickupFoundItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (OnLoadItemsCallback) getParentFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void onItemDeleteClick(ReceiptItemDeleteEvent event){
        ((ReceiptItemsAdapter) getAdapter()).deleteItem(event.getPosition());

        callback.onItemsCountChange(0, getAdapter().getItemCount());
    }

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new PickupFoundItemsTaskLoader(getContext(),
                args.getString(ARG_TABLE_ID),
                args.getStringArrayList(ARG_ITEM_IDS));
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        if(data == null)
            return;

        if(getAdapter() == null) {
            setListShown(true);
            //setAdapter(new ReceiptItemsAdapter(data.getCells()));
        }else{
            ((ReceiptItemsAdapter)getAdapter()).refreshItems(data.getCells());
        }

        callback.onItemsCountChange(0, data.getCells().size());
    }

    @Override
    public void reloadItems(List<String> codes) {
        getArguments().putStringArrayList(ARG_ITEM_IDS, (ArrayList<String>) codes);

        getLoaderManager().restartLoader(0, getArguments(), this);
    }
}
