package com.android.ocasa.core.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.android.ocasa.core.R;

/**
 * Created by ignacio on 25/05/2015.
 */
public class MenuActivity extends BarActivity {

    private ActionBarDrawerToggle drawerToggle;

    public interface MenuListener{
        public void onMenuClosed();
        public void onMenuOpened();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){

        drawerToggle = new ActionBarDrawerToggle(
                this,
                getDrawerLayout(),
                getToolbar(),
                R.string.app_name,
                R.string.app_name){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                Fragment menu = getSupportFragmentManager().findFragmentByTag("Menu");

                if(menu != null){
                    ((MenuListener) menu).onMenuClosed();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Fragment menu = getSupportFragmentManager().findFragmentByTag("Menu");

                if(menu != null){
                    ((MenuListener) menu).onMenuOpened();
                }
            }
        };


        getDrawerLayout().setDrawerListener(drawerToggle);
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void setMenu(Fragment fragment){
        pushFragment(fragment, "Menu", R.id.menu_container);
    }

    public void closeMenu(){
        getDrawerLayout().closeDrawer(GravityCompat.START);
    }

    public void openMenu(){
        getDrawerLayout().openDrawer(GravityCompat.START);
    }

    public boolean isMenuOpen(){
        return getDrawerLayout().isDrawerOpen(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
