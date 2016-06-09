package com.android.ocasa.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.StatusDAO;
import com.android.ocasa.fragment.CreateHeaderReceiptFragment;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Status;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FormViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ignacio Oviedo on 17/03/16.
 */
public class CreateReceiptActivity extends BarActivity{

    public static final String EXTRA_RECEIPT_ID = "receipt_id";

    public boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        if(extras == null)
            return;

        pushFragment(CreateHeaderReceiptFragment.newInstance(extras.getLong(EXTRA_RECEIPT_ID)), "CreateReceipt");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_receipt, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        CreateHeaderReceiptFragment frag =
                (CreateHeaderReceiptFragment) getSupportFragmentManager().findFragmentByTag("CreateReceipt");

        if(frag != null){
            frag.onBackPressed();
            return;
        }

        checkDeleteReceipt();
        super.onBackPressed();
        ReceiptCacheManager.getInstance().clearCache();
    }

    @Override
    protected void onDestroy() {
        checkDeleteReceipt();
        super.onDestroy();
    }

    private void checkDeleteReceipt(){
        Bundle extras = getIntent().getExtras();

        if(!isSaved){
            new ReceiptDAO(this).delete(extras.getLong(EXTRA_RECEIPT_ID));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                return true;
            case R.id.status:
                Intent intent = new Intent(this, ReceiptStatusActvity.class);
                intent.putExtra(ReceiptStatusActvity.EXTRA_RECEIPT_ID,
                        getIntent().getExtras().getLong(EXTRA_RECEIPT_ID));
                startNewActivity(intent);
                return true;

//            case android.R.id.home:
//                ReceiptCacheManager.getInstance().clearCache();
//
//                NavUtils.navigateUpFromSameTask(this);
//                overridePendingTransition(com.android.ocasa.core.R.anim.activity_scale_in, com.android.ocasa.core.R.anim.activity_translatex_out);
//                return true;
            default:
                return false;
        }
    }

    private void save(){
        isSaved = true;

        CreateHeaderReceiptFragment createReceipt =
                (CreateHeaderReceiptFragment) getSupportFragmentManager().findFragmentByTag("CreateReceipt");

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
//
        RecordDAO recordDAO = RecordDAO.getInstance(this);
        FieldDAO fieldDAO = new FieldDAO(this);
        for (long id : ids){
            Record record = recordDAO.findById(id);
            record.addReceipt(new ReceiptItem(receipt, record));
            records.add(record);

            ReceiptCacheManager.getInstance().fillRecord(record);

            for (Field headerField : receipt.getHeaderValues()) {
                Field field = record.getFieldForColumn(headerField.getColumn().getId());
                field.setValue(headerField.getValue());
            }

            List<History> histories = new ArrayList<>();

            for (Field field : record.getFields()){

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

        recordDAO.save(records);

        NavUtils.navigateUpFromSameTask(this);
    }

}
