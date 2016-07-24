package com.android.ocasa.receipt.edit;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.ReceiptItemDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.StatusDAO;
import com.android.ocasa.mail.Mail;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Status;
import com.android.ocasa.receipt.base.BaseReceiptActivity;
import com.android.ocasa.session.SessionManager;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 07/07/16.
 */
public class EditReceiptActivity extends BaseReceiptActivity implements AlertDialogFragment.OnAlertClickListener {

    public boolean isSaved = false;

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

        if(item.getItemId() == R.id.save){
            showSaveAlert();
            return true;
        }else if(item.getItemId() == android.R.id.home){
            showAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        showAlert();
        //super.onBackPressed();
    }

    private void showAlert() {
        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("Salir", "¿Desea descartar los cambios?");

        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    private void showSaveAlert(){
        AlertDialogFragment.newInstance("Guardar","¿Desea guardar los cambios?", "Guardar", null, "Contabilizar").show(getSupportFragmentManager(), "SaveConfirmation");
    }

    private void save(boolean close){
        isSaved = true;

        EditReceiptFragment createReceipt =
                (EditReceiptFragment) getSupportFragmentManager().findFragmentByTag("EditReceipt");

        FormViewModel receiptHeader = createReceipt.getReceiptHeader();

//        Map<String, String> values = headerFragment.getHeaderValues();
//
//        ReceiptItemsFragment itemsFragment = (ReceiptItemsFragment) adapter.getItem(1);
        long[] ids = createReceipt.getRecordIds();
//
        if(ids == null || ids.length == 0){
            Toast.makeText(this, "Debe cargar algun Item", Toast.LENGTH_LONG).show();
            return;
        }
//
        ReceiptDAO dao = new ReceiptDAO(this);

        Receipt receipt = dao.findById(receiptHeader.getId());
        receipt.setConfirmed(true);

        if(close)
            receipt.setClose();
        else
            receipt.setOpen();

        receipt.setCreatedAt(DateTimeHelper.formatDateTime(new Date()));

        Status status = new Status();
        status.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
        status.setTimezone(DateTimeHelper.getDeviceTimezone());
        status.setReceipt(receipt);

        Location location = createReceipt.getLastLocation();

        if(location != null){
            status.setLatitude(location.getLatitude());
            status.setLongitude(location.getLongitude());
        }

        new StatusDAO(this).save(status);

        receipt.setStatus(status);

        dao.update(receipt);

//        receipt.setNumber((int) (Math.random() * 100));
//        receipt.setAction(headerFragment.getAction());
//        receipt.setValidityDate(DateTimeHelper.formatDateTime(headerFragment.getValidityDate()));
//
//        new ReceiptDAO(this).save(receipt);
//
        List<Record> records = new ArrayList<>();



        RecordDAO recordDAO = RecordDAO.getInstance(this);
        FieldDAO fieldDAO = new FieldDAO(this);
        ReceiptItemDAO itemDAO = new ReceiptItemDAO(this);
        itemDAO.deleteForReceipt(receipt.getId());
        List<ReceiptItem> items = new ArrayList<>();

        for (long id : ids){
            Record record = recordDAO.findById(id);

            ReceiptItem item = new ReceiptItem(receipt, record);
            items.add(item);

            itemDAO.save(item);
            records.add(record);

            ReceiptCacheManager.getInstance().fillRecord(record);

            for (Field headerField : receipt.getHeaderValues()) {
                Field field = record.getFieldForColumn(headerField.getColumn().getId());
                field.setValue(headerField.getValue());
            }

            List<History> histories = new ArrayList<>();

            for (Field field : record.getFields()) {

                History history = new History();
                history.setValue(field.getValue());
                history.setField(field);
                history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
                history.setTimeZone(DateTimeHelper.getDeviceTimezone());
                history.setReceipt(receipt);

                histories.add(history);
            }

            new HistoryDAO(this).save(histories);

            fieldDAO.update(record.getFields());

        }

        receipt.setItems(items);

        recordDAO.save(records);

        if(close){
            sendMail(receipt);
        }

        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onPosiviteClick(String tag) {

        if(tag.equalsIgnoreCase("Dialog")){
            finish();
            return;
        }

        save(tag.equalsIgnoreCase("CloseConfrimation"));
    }

    @Override
    public void onNeutralClick(String tag) {
        AlertDialogFragment.newInstance("Contabilizar","¿Esta seguro de contabilizar el comprobante? No podra volver a modificarlo").show(getSupportFragmentManager(), "CloseConfrimation");
    }

    @Override
    public void onNegativeClick(String tag) {

    }

    private void sendMail(Receipt receipt){

        new AsyncTask<Receipt, Void, Void>(){

            @Override
            protected Void doInBackground(Receipt... receipts) {

                Receipt receipt = receipts[0];

                Mail m = new Mail("oviedoignacio91@gmail.com", "Igna1991.");

                String[] toArr = {"oviedoignacio91@gmail.com", "juan.sappracone@ocasa.com"};
                m.setTo(toArr);
                m.setFrom("example@example.com");
                m.setSubject(receipt.getAction().getName());

                StringBuilder body = new StringBuilder();

                String sellerId = null;

                for (Field headerField : receipt.getHeaderValues()){
                    if(headerField.getColumn().getId().equalsIgnoreCase("706")){
                        sellerId = headerField.getValue();
                    }
                }

                body.append("User:" + SessionManager.getInstance().getUser() + "\n");
                body.append("Seller:" + sellerId + "\n");

                for (ReceiptItem item : receipt.getItems()){
                    body.append(item.getRecord().getPrimaryKey().getValue() + "\n");
                }

                m.setBody(body.toString());

                try {
                    if(m.send()) {
                        Log.v("MailApp", "Email sent");
                    } else {
                        Log.v("MailApp", "Could not send email");
                    }
                } catch(Exception e) {
                    Log.e("MailApp", "Could not send email", e);
                }

                return null;
            }
        }.execute(receipt);


    }
}
