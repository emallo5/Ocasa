package com.android.ocasa.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.home.menu.MenuFragment;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Table;
import com.android.ocasa.receipt.list.ReceiptListFragment;

/**
 * Ignacio Oviedo on 11/01/16.
 */
public class HomeActivity extends MenuActivity implements MenuFragment.OnMenuItemClickListener{

    private Table selectTable;
    private Action selectAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            return;

        setMenu(Fragment.instantiate(this, MenuFragment.class.getName()));
    }

    @Override
    public void onTableClick(Table table) {

        closeMenu();

        if(selectTable != null && selectTable.getId().equals(table.getId()))
            return;

        selectAction = null;
        selectTable = table;

        pushFragment(HomeFragment.newInstance(table.getId()), "Home");
    }

    @Override
    public void onActionClick(Action action) {
        closeMenu();

        if(selectAction != null && selectAction.getId().equals(action.getId()))
            return;

        selectTable = null;
        selectAction = action;

        pushFragment(ReceiptListFragment.newInstance(action.getId()), "Receipt");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
