package com.android.ocasa.record.create;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;

/**
 * Created by ignacio on 09/06/16.
 */
public class CreateRecordActivity extends BarActivity {

    public static final String EXTRA_TABLE_ID = "table_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        pushFragment(CreateRecordFragment.newInstance(extras.getString(EXTRA_TABLE_ID)), "Create");
    }
}
