package com.android.ocasa.session;

import android.content.Intent;

import com.android.ocasa.loginflow.LoginFlowActivity;
import com.codika.androidmvp.activity.BaseMvpActivity;

/**
 * Created by ignacio on 08/09/16.
 */
public abstract class SessionActivity<V  extends SessionView, P extends SessionPresenter<V>> extends BaseMvpActivity<V, P> implements SessionView{

    @Override
    public void onUnauthorizedError() {
        getPresenter().cleanSession();
        goLoginScreen();
    }

    private void goLoginScreen(){
        Intent loginIntent = new Intent(this, LoginFlowActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }
}
