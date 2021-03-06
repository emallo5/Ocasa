package com.android.ocasa.record.detail;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;

/**
 * Created by ignacio on 28/01/16.
 */
public class DetailRecordActivity extends BarActivity {

    public static final String EXTRA_RECORD_ID = "record_id";
    public static final String EXTRA_RECORDS_ID = "records_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        pushFragment(DetailRecordFragment.newInstance(extras.getLong(EXTRA_RECORD_ID)), "Detail");
    }
}
