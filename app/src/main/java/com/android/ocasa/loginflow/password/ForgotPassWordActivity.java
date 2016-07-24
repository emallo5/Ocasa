package com.android.ocasa.loginflow.password;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.loginflow.password.ForgotPasswordFragment;

/**
 * Created by ignacio on 04/02/16.
 */
public class ForgotPassWordActivity extends BarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getToolbar().setTitle(R.string.forgot_password_bar_title);

        if(savedInstanceState != null)
            return;

        pushFragment(Fragment.instantiate(this, ForgotPasswordFragment.class.getName()), "ForgotPassword");
    }
}
