package com.android.ocasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.ocasa.R;
import com.android.ocasa.fragment.ValidityDateDialogFragment;

import java.util.Calendar;

/**
 * Created by ignacio on 25/02/16.
 */
public class ValidityDateActivity extends AppCompatActivity implements ValidityDateDialogFragment.SendFormListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, ValidityDateDialogFragment.newInstance(), "ValidityDate")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, com.android.ocasa.core.R.anim.slide_down_dialog);
    }

    @Override
    public void send(Calendar validityDate) {
        Intent intent = new Intent();
        intent.putExtra("ValidityDate", validityDate);

        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.no_change, com.android.ocasa.core.R.anim.slide_down_dialog);
    }
}
