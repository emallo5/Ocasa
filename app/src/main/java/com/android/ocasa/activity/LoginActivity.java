package com.android.ocasa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.fragment.LoginFragment;
import com.android.ocasa.fragment.SyncFragment;

/**
 * Created by ignacio on 14/01/16.
 */
public class LoginActivity extends BaseActivity implements LoginFragment.LoginCallback, SyncFragment.SyncCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pushFragment(Fragment.instantiate(this, LoginFragment.class.getName()), "Login", R.id.container);
    }

    @Override
    public void onLogin() {
        pushFragment(Fragment.instantiate(this, SyncFragment.class.getName()), "Sync", R.id.container, true);
    }


    @Override
    public void onSync() {
        goToNextScreen();
    }

    private void goToNextScreen(){
        startNewActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
