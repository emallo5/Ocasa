package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.UpdateReceiptItemFragment;

/**
 * Created by Emiliano Mallo on 31/03/16.
 */
public class UpdateReceiptItemActivity extends BarActivity {

    public static final String EXTRA_RECORD_ID = "record_id";
    public static final String EXTRA_ACTION_ID = "action_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        pushFragment(UpdateReceiptItemFragment.newInstance(
                extras.getLong(EXTRA_RECORD_ID), extras.getString(EXTRA_ACTION_ID)),
                "Detail");
    }
}
