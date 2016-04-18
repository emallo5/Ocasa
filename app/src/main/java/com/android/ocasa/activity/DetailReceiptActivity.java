package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.R;
import com.android.ocasa.adapter.RecieptPagerAdapter;
import com.android.ocasa.core.activity.TabActivity;
import com.android.ocasa.fragment.DetailHeaderReceiptFragment;
import com.android.ocasa.fragment.DetailReceiptItemsFragment;

/**
 * Created by Emiliano Mallo on 28/03/16.
 */
public class DetailReceiptActivity extends TabActivity {

    public static final String EXTRA_RECEIPT_ID = "receipt_id";

    private RecieptPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        if(extras == null)
            return;

        adapter = new RecieptPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.create_receipt_tabs));
        adapter.addFragment(DetailHeaderReceiptFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)));
        adapter.addFragment(DetailReceiptItemsFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)));

        setPagerAdapter(adapter);
    }
}
