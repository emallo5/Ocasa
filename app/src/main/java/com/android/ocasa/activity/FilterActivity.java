package com.android.ocasa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.ocasa.R;
import com.android.ocasa.util.Filter;
import com.android.ocasa.util.FilterDialogFragment;

/**
 * Created by ignacio on 15/02/16.
 */
public class FilterActivity extends AppCompatActivity implements FilterDialogFragment.ApplyFilterListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();

        String tableId = getIntent().getStringExtra("table_id");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, FilterDialogFragment.newInstance(tableId), "Filter")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, com.android.ocasa.core.R.anim.slide_down_dialog);
    }

    @Override
    public void apply(Filter filter) {

        Intent intent = new Intent();
        intent.putExtra("Filter", filter);

        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.no_change, com.android.ocasa.core.R.anim.slide_down_dialog);
    }
}
