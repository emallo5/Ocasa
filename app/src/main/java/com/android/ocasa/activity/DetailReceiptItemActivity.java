package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.DetailReceiptItemFragment;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class DetailReceiptItemActivity extends BarActivity {

    public static final String EXTRA_RECORD_ID = "record_id";
    public static final String EXTRA_RECEIPT_ID = "receipt_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        pushFragment(DetailReceiptItemFragment.newInstance(
                extras.getLong(EXTRA_RECORD_ID), extras.getLong(EXTRA_RECEIPT_ID)),
                "Detail");
    }
}
