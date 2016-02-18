package com.android.ocasa.activity;

import android.content.Intent;

import com.android.ocasa.barcode.BarcodeActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by ignacio on 04/02/16.
 */
public class ReadFieldActvivity extends BarcodeActivity {

    public static final String QR_FIELD_TAG = "field_tag";

    @Override
    public void onBarcodeSelected(Barcode barcode) {
        Intent data = new Intent();
        data.putExtra(BarcodeObject, barcode);
        data.putExtra(QR_FIELD_TAG, getIntent().getStringExtra(QR_FIELD_TAG));
        setResult(CommonStatusCodes.SUCCESS, data);
        finish();
    }
}
