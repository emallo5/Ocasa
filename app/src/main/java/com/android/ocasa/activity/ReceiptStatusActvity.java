package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.ReceiptStatusFragment;

/**
 * Created by ignacio on 26/05/16.
 */
public class ReceiptStatusActvity extends BarActivity {

    public static final String EXTRA_RECEIPT_ID = "receipt_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        if(savedInstanceState == null)
            pushFragment(ReceiptStatusFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)), "Status");
    }
}
