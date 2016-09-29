package com.android.ocasa.loginflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.loginflow.login.LoginFragment;
import com.android.ocasa.sync.SyncActivity;

/**
 * Created by ignacio on 14/01/16.
 */
public class LoginFlowActivity extends BaseActivity implements LoginFragment.LoginCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pushFragment(Fragment.instantiate(this, LoginFragment.class.getName()), "Login", R.id.container);
    }

    @Override
    public void onLogin() {
        startActivity(new Intent(this, SyncActivity.class));
        finish();
    }
}
