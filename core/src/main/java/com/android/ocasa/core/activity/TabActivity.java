package com.android.ocasa.core.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.ocasa.core.R;

/**
 * Ignacio Oviedo on 17/03/16.
 */
public class TabActivity extends BaseActivity {

    private ViewPager pager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public void setPagerAdapter(FragmentPagerAdapter adapter){
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    public TabLayout getTabLayout(){
        return tabLayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.activity_scale_in, R.anim.activity_translatex_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
