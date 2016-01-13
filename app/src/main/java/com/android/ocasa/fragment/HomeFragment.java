package com.android.ocasa.fragment;

import android.os.Bundle;

import com.android.ocasa.core.fragment.ListFragment;

/**
 * Created by ignacio on 11/01/16.
 */
public class HomeFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListShown(true);
    }
}
