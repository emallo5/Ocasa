package com.android.ocasa.receipt.header;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.receipt.header.EditHeaderReceiptFragment;

/**
 * Created by ignacio on 19/05/16.
 */
public class EditHeaderReceiptActivity extends BarActivity {

    public static final String EXTRA_ACTION_ID = "action_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        pushFragment(EditHeaderReceiptFragment.newInstance(extras.getString(EXTRA_ACTION_ID)), "Edit");
    }
}
