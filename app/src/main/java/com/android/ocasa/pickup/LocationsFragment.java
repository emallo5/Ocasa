package com.android.ocasa.pickup;

import android.content.Intent;
import android.os.Bundle;

import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.home.HomeFragment;

/**
 * Created by Emiliano Mallo on 21/04/16.
 */
public class LocationsFragment extends HomeFragment {

    public static LocationsFragment newInstance(String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);

        LocationsFragment fragment = new LocationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(ReceiptItemEvent event) {
        Intent intent = new Intent(getActivity(), SyncPickupItemsActivity.class);
        intent.putExtra(SyncPickupItemsActivity.EXTRA_RECORD_ID, event.getRecordId());
        ((BaseActivity) getActivity()).startNewActivity(intent);
    }
}
