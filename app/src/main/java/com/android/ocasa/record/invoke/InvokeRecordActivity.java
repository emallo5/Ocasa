package com.android.ocasa.record.invoke;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;

/**
 * Created by ignacio on 14/07/16.
 */
public class InvokeRecordActivity extends BarActivity {

    public static final String EXTRA_TABLE_ID = "table_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        pushFragment(InvokeRecordFragment.newInstance(extras.getString(EXTRA_TABLE_ID)), "Invoke");
    }
}
