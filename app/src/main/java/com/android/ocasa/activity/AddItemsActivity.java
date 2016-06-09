package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.AddItemsFragment;

/**
 * Created by Emiliano Mallo on 21/03/16.
 */
public class AddItemsActivity extends BarActivity {

    public static final String EXTRA_ACTION_ID = "action_id";
    public static final String EXTRA_EXCLUDE_IDS = "exclude_ids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

//        pushFragment(AddItemsFragment.newInstance(extras.getString(EXTRA_RECEIPT_ID),
//                extras.getLongArray(EXTRA_EXCLUDE_IDS)), "Items");

    }
}
