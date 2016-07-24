package com.android.ocasa.receipt.status;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.ocasa.core.activity.BarActivity;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
