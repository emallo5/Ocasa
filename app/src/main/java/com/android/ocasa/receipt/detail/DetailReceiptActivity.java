package com.android.ocasa.receipt.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.android.ocasa.R;
import com.android.ocasa.receipt.base.BaseReceiptActivity;

/**
 * Created by ignacio on 07/07/16.
 */
public class DetailReceiptActivity extends BaseReceiptActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras == null)
            return;

        pushFragment(DetailReceiptFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)), "DetailReceipt");

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.save).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
