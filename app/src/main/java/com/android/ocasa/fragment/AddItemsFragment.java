package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.android.ocasa.R;
import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.activity.ReadFieldActvivity;
import com.android.ocasa.adapter.RecordAdapterTest;
import com.android.ocasa.barcode.BarcodeActivity;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.event.RecordLongClickEvent;
import com.android.ocasa.loader.ActionRecordTaskLoader;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.TableViewModel;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Emiliano Mallo on 21/03/16.
 */
public class AddItemsFragment extends FilterRecordListFragment implements ActionMode.Callback {

    static final String ARG_ACTION_ID = "action_id";
    static final String ARG_EXCLUDE_IDS = "exclude_ids";

    static final int REQUEST_QR_SCANNER = 1000;

    private ActionMode actionMode;

    public static AddItemsFragment newInstance(String actionId, long[] exludeIds) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);
        args.putLongArray(ARG_EXCLUDE_IDS, exludeIds);

        AddItemsFragment fragment = new AddItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new ActionRecordTaskLoader(getActivity(), args.getString(ARG_ACTION_ID),
                args.getString(ARG_SEARCH_QUERY),
                args.getLongArray(ARG_EXCLUDE_IDS)) {
        };
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        super.onLoadFinished(loader, data);

        setTitle(data.getName());
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_multiple_selection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        this.actionMode = actionMode;
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.add){
            returnRecords();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        ((RecordAdapterTest)getAdapter()).clearSelections();
        this.actionMode = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.qr_scanner){
            Intent intent =  new Intent(getActivity(), ReadFieldActvivity.class);

            startActivityForResult(intent, REQUEST_QR_SCANNER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_QR_SCANNER){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data == null)
                    return;

                Barcode barcode = data.getParcelableExtra(BarcodeActivity.BarcodeObject);

                submitQuery(barcode.displayValue);
            }
        }
    }

    @Override
    public void onItemClick(ReceiptItemEvent event) {
        RecordAdapterTest adapter = (RecordAdapterTest) getAdapter();

        if(actionMode != null){
            adapter.toggleSelection(event.getPosition());
            if(adapter.getSelectedItemCount() == 0){
                actionMode.finish();
            }
        }
    }

    @Subscribe
    public void onLongItemClick(RecordLongClickEvent event){

        if(actionMode != null)
            return;

        getActivity().startActionMode(this);
        ((RecordAdapterTest)getAdapter()).toggleSelection(event.getPosition());
    }

    private void returnRecords(){

        long[] checkedIds = ((RecordAdapterTest)getAdapter()).getSelectedItemIds();

        Intent data = new Intent();

        data.putExtra(DetailRecordActivity.EXTRA_RECORDS_ID, checkedIds);

        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
        getActivity().overridePendingTransition(com.android.ocasa.core.R.anim.activity_scale_in,
                com.android.ocasa.core.R.anim.activity_translatex_out);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_receipt_items, menu);

    }
}
