package com.android.ocasa.receipt.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.ocasa.R;
import com.android.ocasa.receipt.status.ReceiptStatusActvity;
import com.android.ocasa.core.activity.BarActivity;

/**
 * Created by ignacio on 07/07/16.
 */
public class BaseReceiptActivity extends BarActivity{

    public static final String EXTRA_RECEIPT_ID = "receipt_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.status){
            Intent intent = new Intent(this, ReceiptStatusActvity.class);
            intent.putExtra(ReceiptStatusActvity.EXTRA_RECEIPT_ID,
                    getIntent().getExtras().getLong(EXTRA_RECEIPT_ID));
            startNewActivity(intent);
            return true;
        }

        return false;
    }
}
