package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.DetailRecordFragment;

/**
 * Created by ignacio on 28/01/16.
 */
public class DetailRecordActivity extends BarActivity {

    public static final String EXTRA_RECORD_ID = "record_id";
    public static final String EXTRA_RECORDS_ID = "records_id";
    public static final String EXTRA_MULTIPLE_EDIT = "multiple_edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        if(extras.containsKey(EXTRA_MULTIPLE_EDIT)) {
            pushFragment(DetailRecordFragment.newInstance(extras.getLongArray(EXTRA_RECORDS_ID)), "Detail");
        }else{
            pushFragment(DetailRecordFragment.newInstance(extras.getLong(EXTRA_RECORD_ID)), "Detail");
        }
    }
}
