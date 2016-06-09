package com.android.ocasa.pickup;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.model.Table;

import java.util.ArrayList;

/**
 * Created by Emiliano Mallo on 21/04/16.
 */
public class LocationsActivity extends BarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            return;

        //Table table = new ArrayList<>(new CategoryDAO(this).findAll().get(0).getTables()).get(0);

        pushFragment(LocationsFragment.newInstance("600"), "Locations");
    }
}
