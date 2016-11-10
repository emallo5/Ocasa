package com.android.ocasa.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.MenuActivity;
import com.android.ocasa.home.menu.MenuFragment;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Table;
import com.android.ocasa.receipt.list.ReceiptListFragment;
import com.android.ocasa.settings.SettingsActivity;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.viewmodel.OptionViewModel;

/**
 * Ignacio Oviedo on 11/01/16.
 */
public class HomeActivity extends MenuActivity implements MenuFragment.OnMenuItemClickListener,
AlertDialogFragment.OnAlertClickListener{

    private Table selectTable;
    private Action selectAction;

    private String selectOptionId;

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
    public void onOptionClick(OptionViewModel option) {
        closeMenu();

        if(selectOptionId != null && selectOptionId.equals(option.getId())){
            return;
        }

        selectOptionId = option.getId();

        if(option.isTable()){
            pushFragment(HomeFragment.newInstance(option.getId()), "Home");
        }else{
            pushFragment(ReceiptListFragment.newInstance(option.getId()), "Receipt");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

        if(isMenuOpen()){
            closeMenu();
            return;
        }

        showExitAlert();
    }

    private void showExitAlert(){
        AlertDialogFragment.newInstance("Salir", "Desea salir de la aplicación", "Salir", null, null)
                .show(getSupportFragmentManager(), "Exit");
    }

    @Override
    public void onPosiviteClick(String tag) {
        finish();
    }

    @Override
    public void onNeutralClick(String tag) {

    }

    @Override
    public void onNegativeClick(String tag) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
