package com.android.ocasa.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.android.ocasa.sync.SyncIntentSerivce;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.ConfigHelper;
import com.android.ocasa.util.ConnectionUtil;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.util.ReceiptCounterHelper;
import com.android.ocasa.util.SyncUtil;
import com.android.ocasa.util.TripHelper;
import com.codika.androidmvp.activity.BaseMvpActivity;

import java.io.File;

/**
 * Created by ignacio on 10/11/16.
 */

public class SettingsActivity extends BaseMvpActivity<SettingsView, SettingsPresenter> implements SettingsView,
AlertDialogFragment.OnAlertClickListener{

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
                getPresenter().checkLogout();
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionUtil.isInternetAvailable(SettingsActivity.this))
                    Toast.makeText(SettingsActivity.this, "Chequee su conexión a internet", Toast.LENGTH_SHORT).show();
                else {
                    showProgress();
                    getPresenter().sync();
                }
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

    private void showProgress() {
        ProgressDialogFragment pdf = ProgressDialogFragment.newInstance("Sincronizando...");
        pdf.setCancelable(false);
        pdf.show(getSupportFragmentManager(), "Sync");
    }

    private void hideProgress() {
        DialogFragment syncDialog = (DialogFragment) getSupportFragmentManager().findFragmentByTag("Sync");
        if(syncDialog != null){
            syncDialog.dismiss();
        }

    }

    @Override
    public void onSyncSuccess() {
        hideProgress();
        if (SyncUtil.getInstance().wasError())
            AlertDialogFragment.newInstance("Atención!", "La sincronización falló por problemas de señal, vuelva a intentarlo.").show(getSupportFragmentManager(), "error");
        else
            Toast.makeText(this, "Sincronizado exitosamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSyncError() {
        hideProgress();
        Toast.makeText(this, "Error al sincronizar, intente mas tarde", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutSuccess() {
        stopSyncService();
        ReceiptCounterHelper.getInstance().setTotalItems(0);
        TripHelper.clearTripMovements();
        TripHelper.setLastMovement(false);
        removeOcasaFiles();
        hideLogoutProgress();
        goLoginScreen();
    }

    @Override
    public void onLogoutError(String error) {
        hideLogoutProgress();
        AlertDialogFragment.newInstance("Error!", error).show(getSupportFragmentManager(), "errorLogout");
    }

    @Override
    public void canLogout() {
        showLogoutProgress();
        stopSyncService();
        getPresenter().logout();
    }

    @Override
    public void needUpload() {
        AlertDialogFragment
                .newInstance("Hay comprobantes para enviar", "Desea sincronizarlos?")
                .show(getSupportFragmentManager(), "Alert");
    }

    @Override
    public void onUploadSuccess() {
        hideProgress();
        if (!ConnectionUtil.isInternetAvailable(this)) {
            notConnection();
            return;
        }
        Toast.makeText(this, "Ahora puede Cerrar sesión", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadError() {
        hideProgress();
        Toast.makeText(this, "Hubo un error, intente mas tarde!", Toast.LENGTH_SHORT).show();
    }

    private void stopSyncService() {
        stopService(new Intent(this, SyncIntentSerivce.class));
    }

    private void goLoginScreen(){
        Intent loginIntent = new Intent(this, LoginFlowActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void removeOcasaFiles() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Ocasa");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    @Override
    public void notConnection() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 500);
        Toast.makeText(this, "Chequee su conexión a Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPosiviteClick(String tag) {
        showProgress();
        getPresenter().uploadReceipts(this);
    }

    @Override
    public void onNeutralClick(String tag) {

    }

    @Override
    public void onNegativeClick(String tag) {

    }
}
