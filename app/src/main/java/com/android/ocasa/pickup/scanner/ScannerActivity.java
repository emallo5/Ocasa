package com.android.ocasa.pickup.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Record;
import com.android.ocasa.pickup.util.PickupItemConfirmationDialog;
import com.android.ocasa.util.InformationDialogFragment;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.Set;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Emiliano Mallo on 21/04/16.
 */
public class ScannerActivity extends BaseActivity implements ZBarScannerView.ResultHandler,
        PickupItemConfirmationDialog.OnConfirmationListener,
        InformationDialogFragment.OnIformationClickListener{

    public static final String EXTRA_RESULT_CODES = "codes";
    public static final String EXTRA_RESULT_CODE = "code";

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private TextView countView;
    private TextView lastCode;
    private Button finishButton;
    private ZBarScannerView mScannerView;

    private MediaPlayer checkSound;

    private Set<String> codes;

    private Set<Long> recordIds;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mScannerView = (ZBarScannerView) findViewById(R.id.scanner_view);
        countView = (TextView) findViewById(R.id.count);
        lastCode = (TextView) findViewById(R.id.last_code);
        finishButton = (Button) findViewById(R.id.finish);

        setListener();

        checkSound = MediaPlayer.create(this, R.raw.check_in_sound);

        codes = new HashSet<>();
        recordIds = new HashSet<>();

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    private void setListener(){
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliverResponse();
            }
        });
    }

    private void deliverResponse(String code){

        Intent data = new Intent();
        data.putExtra(EXTRA_RESULT_CODE, code);

        setResult(RESULT_OK, data);

        finish();
    }


    private void deliverResponse(){

        Intent data = new Intent();

        Long[] array = recordIds.toArray(new Long[recordIds.size()]);

        data.putExtra(EXTRA_RESULT_CODES, ArrayUtils.toPrimitive(array));

        setResult(RESULT_OK, data);

        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        checkSound.start();
        //handleCode(rawResult.getContents());
        new CheckExistTask().execute(rawResult.getContents());

    }

    @Override
    public void onCancel() {
        mScannerView.resumeCameraPreview(ScannerActivity.this);
    }

    @Override
    public void onAdd(String code) {
//        handleCode(code);
        mScannerView.resumeCameraPreview(ScannerActivity.this);
    }

    private void handleCode(String code, long recordId){

        if(codes.contains(code)){
            InformationDialogFragment.newInstance("Codigo ya leido", "El codigo " + code + " ya fue leido!").show(getSupportFragmentManager(), "ConfirmationDialog");
            return;
        }

        codes.add(code);
        recordIds.add(recordId);
        countView.setText("Codigos leidos: " + codes.size());
        lastCode.setText(code);
        mScannerView.resumeCameraPreview(ScannerActivity.this);
        //deliverResponse(code);
    }

    @Override
    public void onCloseDialog() {
        mScannerView.resumeCameraPreview(ScannerActivity.this);
    }

    public class CheckResult{
        private boolean exists;
        private String code;
        private long recordId;

        private CheckResult(boolean exists, String code, long recordId) {
            this.exists = exists;
            this.code = code;
            this.recordId = recordId;
        }

        public String getCode() {
            return code;
        }

        public boolean isExists() {
            return exists;
        }

        public long getRecordId() {
            return recordId;
        }

        public void setRecordId(long recordId) {
            this.recordId = recordId;
        }
    }

    public class CheckExistTask extends AsyncTask<String, Void, CheckResult>{

        @Override
        protected CheckResult doInBackground(String... strings) {
            Record record = RecordDAO.getInstance(ScannerActivity.this).findForTableAndValueId("700", strings[0]);

            return new CheckResult(record != null, strings[0], record != null ? record.getId() : 0);
        }

        @Override
        protected void onPostExecute(CheckResult s) {
            super.onPostExecute(s);
            if(!s.isExists()){
                PickupItemConfirmationDialog.newInstance(s.getCode()).show(getSupportFragmentManager(), "ConfirmationDialog");
                return;
            }

            handleCode(s.getCode(), s.getRecordId());
        }
    }

}
