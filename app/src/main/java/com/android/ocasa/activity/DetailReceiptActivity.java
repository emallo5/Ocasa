package com.android.ocasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.ocasa.R;
import com.android.ocasa.adapter.RecieptPagerAdapter;
import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.core.activity.TabActivity;
import com.android.ocasa.fragment.CreateHeaderReceiptFragment;
import com.android.ocasa.fragment.DetailHeaderReceiptFragment;
/**
 * Created by Emiliano Mallo on 28/03/16.
 */
public class DetailReceiptActivity extends BarActivity {

    public static final String EXTRA_RECEIPT_ID = "receipt_id";

//    private RecieptPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        if(extras == null)
            return;

        pushFragment(DetailHeaderReceiptFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)), "DetailReceipt");

//        adapter = new RecieptPagerAdapter(getSupportFragmentManager(),
//                getResources().getStringArray(R.array.create_receipt_tabs));
//        adapter.addFragment(DetailHeaderReceiptFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)));
//        adapter.addFragment(DetailReceiptItemsFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)));
//
//        setPagerAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.status){
            Intent intent = new Intent(this, DetailReceiptStatusActivity.class);
            intent.putExtra(DetailReceiptStatusActivity.EXTRA_RECEIPT_ID,
                    getIntent().getExtras().getLong(EXTRA_RECEIPT_ID));
            startNewActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
