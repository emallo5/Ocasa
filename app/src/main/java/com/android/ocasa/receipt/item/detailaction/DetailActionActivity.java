package com.android.ocasa.receipt.item.detailaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.widget.FieldViewAdapter;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.codika.androidmvp.activity.BaseMvpActivity;

import java.util.List;

/**
 * Created by ignacio on 24/10/16.
 */

public class DetailActionActivity extends BarActivity{
    public static final String EXTRA_RECEIPT_ID = "_receipt_id_";
    public static final String EXTRA_RECORD_ID = "_record_id_";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        pushFragment(DetailActionFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID),
                extras.getLong(EXTRA_RECORD_ID)), "DetailAction");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
