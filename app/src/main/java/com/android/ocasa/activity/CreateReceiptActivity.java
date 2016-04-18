package com.android.ocasa.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.adapter.RecieptPagerAdapter;
import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.core.activity.TabActivity;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.fragment.CreateHeaderReceiptFragment;
import com.android.ocasa.fragment.ReceiptItemsFragment;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.util.DateTimeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Ignacio Oviedo on 17/03/16.
 */
public class CreateReceiptActivity extends TabActivity implements ReceiptItemsFragment.OnAddItemsListener{

    public static final String EXTRA_ACTION_ID = "action_id";

    private RecieptPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        if(extras == null)
            return;

        adapter = new RecieptPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.create_receipt_tabs));
        adapter.addFragment(CreateHeaderReceiptFragment.newInstance(extras.getString(EXTRA_ACTION_ID)));
        adapter.addFragment(ReceiptItemsFragment.newInstance(extras.getString(EXTRA_ACTION_ID)));

        setPagerAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_receipt, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ReceiptCacheManager.getInstance().clearCache();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                return true;
            case android.R.id.home:
                ReceiptCacheManager.getInstance().clearCache();

                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(com.android.ocasa.core.R.anim.activity_scale_in, com.android.ocasa.core.R.anim.activity_translatex_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        CreateHeaderReceiptFragment headerFragment = (CreateHeaderReceiptFragment) adapter.getItem(0);
        Map<String, String> values = headerFragment.getHeaderValues();

        ReceiptItemsFragment itemsFragment = (ReceiptItemsFragment) adapter.getItem(1);
        long[] ids = itemsFragment.getRecordIds();

        if(ids == null || ids.length == 0){
            Toast.makeText(this, "Debe cargar algun Item", Toast.LENGTH_LONG).show();
            return;
        }

        Receipt receipt = new Receipt();
        receipt.setNumber((int) (Math.random() * 100));
        receipt.setAction(headerFragment.getAction());
        receipt.setValidityDate(DateTimeHelper.formatDateTime(headerFragment.getValidityDate()));

        new ReceiptDAO(this).save(receipt);

        List<Record> records = new ArrayList<>();

        RecordDAO recordDAO = new RecordDAO(this);
        FieldDAO fieldDAO = new FieldDAO(this);
        for (long id : ids){
            Record record = recordDAO.findById(id);
            record.addReceipt(new ReceiptItem(receipt, record));
            records.add(record);

            ReceiptCacheManager.getInstance().fillRecord(record);

            for (Map.Entry<String, String> pair : values.entrySet()) {
                Field field = record.getFieldForColumn(pair.getKey());
                field.setValue(pair.getValue());
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

        onBackPressed();
    }

    @Override
    public void onItemsCountChange(int count) {
        getTabLayout().getTabAt(1).setText(getString(R.string.create_receipt_tab_items, count));
    }
}
