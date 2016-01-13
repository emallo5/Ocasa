package com.android.ocasa.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.fragment.HomeFragment;

/**
 * Created by ignacio on 11/01/16.
 */
public class HomeActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pushFragment(Fragment.instantiate(this, HomeFragment.class.getName()), "Home");
    }
}
