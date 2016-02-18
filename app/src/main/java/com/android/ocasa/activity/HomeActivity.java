package com.android.ocasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.fragment.HomeFragment;
import com.android.ocasa.fragment.MenuFragment;
import com.android.ocasa.model.Table;

/**
 * Created by ignacio on 11/01/16.
 */
public class HomeActivity extends MenuActivity implements MenuFragment.OnMenuItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            return;

        pushFragment(HomeFragment.newInstance("1"), "Home");
        setMenu(Fragment.instantiate(this, MenuFragment.class.getName()));
    }

    @Override
    public void onItemClick(Table table) {
        closeMenu();
        pushFragment(HomeFragment.newInstance(table.getId()), "Home");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
