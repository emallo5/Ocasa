package com.android.ocasa.receipt.item.update;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.ocasa.core.activity.BarActivity;

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
                extras.getLong(EXTRA_RECORD_ID), extras.getLong(EXTRA_ACTION_ID)),
                "Detail");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
