package com.android.ocasa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class RecieptPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String[] titles;

    public RecieptPagerAdapter(FragmentManager fm, String[] stringArray) {
        super(fm);
        fragments = new ArrayList<>();
        titles = stringArray;
    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
