package com.android.ocasa.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.fragment.HomeFragment;
import com.android.ocasa.fragment.MenuFragment;

/**
 * Created by ignacio on 11/01/16.
 */
public class HomeActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pushFragment(HomeFragment.newInstance("1"), "Home");
        setMenu(Fragment.instantiate(this, MenuFragment.class.getName()));
    }
}
