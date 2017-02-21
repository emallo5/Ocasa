package com.android.ocasa.receipt.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.android.ocasa.receipt.base.BaseReceiptActivity;
import com.android.ocasa.util.AlertDialogFragment;

/**
 * Created by ignacio on 07/07/16.
 */
public class EditReceiptActivity extends BaseReceiptActivity implements AlertDialogFragment.OnAlertClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras == null)
            return;

        pushFragment(EditReceiptFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)), "EditReceipt");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

          if(item.getItemId() == android.R.id.home){
            showAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlert() {
        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("¡ATENCION!", "¿Seguro que desea salir sin ENVIAR los datos?");

        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void onPosiviteClick(String tag) {

        if(tag.equalsIgnoreCase("Dialog")){
            finish();
        }
    }

    @Override
    public void onNeutralClick(String tag) {
        AlertDialogFragment.newInstance("Contabilizar",
                "¿Esta seguro de contabilizar el comprobante? No podra volver a modificarlo")
                .show(getSupportFragmentManager(), "CloseConfirmation");
    }

    @Override
    public void onNegativeClick(String tag) {

    }

}
