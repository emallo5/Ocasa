package com.android.ocasa.pickup;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;

/**
 * Created by Emiliano Mallo on 21/04/16.
 */
public class PickupActivity extends BarActivity {

    public static final String EXTRA_RECORD_ID = "record_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        pushFragment(PickupFragment.newInstance(extras.getLong(EXTRA_RECORD_ID)), "Pickup");
    }
}
