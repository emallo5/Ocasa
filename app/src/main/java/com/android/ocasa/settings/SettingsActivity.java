package com.android.ocasa.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.loginflow.LoginFlowActivity;
import com.android.ocasa.util.ProgressDialogFragment;
import com.codika.androidmvp.activity.BaseMvpActivity;

/**
 * Created by ignacio on 10/11/16.
 */

public class SettingsActivity extends BaseMvpActivity<SettingsView, SettingsPresenter> implements SettingsView{

    private Button logout;
    private Button sync;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initControls();
        setListeners();
    }

    private void initControls(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Configuración");

        logout = (Button) findViewById(R.id.logout);
        sync = (Button) findViewById(R.id.sync);
    }

    private void setListeners(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutProgress();
                getPresenter().logout();
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                getPresenter().sync();
            }
        });
    }

    @Override
    public SettingsView getMvpView() {
        return this;
    }

    @Override
    public Loader<SettingsPresenter> getPresenterLoader() {
        return new SettingsLoader(this);
    }

    private void showLogoutProgress(){
        ProgressDialogFragment.newInstance("Cerrando sesion").show(getSupportFragmentManager(), "Logout");
    }

    private void hideLogoutProgress(){
        DialogFragment syncDialog =
                (DialogFragment) getSupportFragmentManager().findFragmentByTag("Logout");
        if(syncDialog != null){
            syncDialog.dismiss();
        }

    }

    private void showProgress(){
        ProgressDialogFragment.newInstance("Sincronizando").show(getSupportFragmentManager(), "Sync");
    }

    private void hideProgress(){
        DialogFragment syncDialog =
                (DialogFragment) getSupportFragmentManager().findFragmentByTag("Sync");
        if(syncDialog != null){
            syncDialog.dismiss();
        }

    }

    @Override
    public void onSyncSuccess() {
        hideProgress();
        Toast.makeText(this, "Sincronizado exitosamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutSuccess() {
        hideLogoutProgress();
        goLoginScreen();
    }

    private void goLoginScreen(){
        Intent loginIntent = new Intent(this, LoginFlowActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
