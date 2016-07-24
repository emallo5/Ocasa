package com.android.ocasa.core.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.ocasa.core.R;

/**
 * Created by ignacio on 25/05/2015.
 */
public class BarActivity extends BaseActivity {

    protected Toolbar toolbar;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar);

        init();
    }

    private void init(){

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);

        toolbar.setTitle("");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void pushFragment(Fragment fragment, String tag){
        pushFragment(fragment, tag, R.id.container, false);
    }

    public void pushFragment(Fragment fragment, String tag, boolean anim){
        pushFragment(fragment, tag, R.id.container, anim, false);
    }

    public void pushFragment(Fragment fragment, String tag, boolean anim, boolean addToBackStack){
        pushFragment(fragment, tag, R.id.container, anim, addToBackStack);
    }

    public void showFragment(Fragment fragment, String tag, boolean addToBackStack){
        showFragment(fragment, tag, R.id.container, addToBackStack);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                    return true;
                }

                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.activity_scale_in, R.anim.activity_translatex_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
