package com.android.ocasa.fragment;

import android.os.Bundle;

/**
 * Created by ignacio on 15/02/16.
 */
public class ComboTableFragment extends TableRecordListFragment {

    public static ComboTableFragment newInstance(String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);

        ComboTableFragment fragment = new ComboTableFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
