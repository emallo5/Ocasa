package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;

import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.adapter.AddItemsReceiptAdapter;
import com.android.ocasa.adapter.RecordAdapterTest;
import com.android.ocasa.core.fragment.RecyclerListFragment;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.loader.ActionRecordTaskLoader;
import com.android.ocasa.viewmodel.TableViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Emiliano Mallo on 21/03/16.
 */
public class AddItemsFragment extends RecyclerListFragment implements LoaderManager.LoaderCallbacks<TableViewModel> {

    static final String ARG_RECEIPT_ID = "receipt_id";
    static final String ARG_SEARCH_QUERY = "search_query";
    static final String ARG_EXCLUDE_IDS = "exclude_ids";

    static final int REQUEST_QR_SCANNER = 1000;

    private ActionMode actionMode;

    private OnItemAddedListener callback;

    public interface OnItemAddedListener{
        void onItemAdded(long recordId);
    }

    public static AddItemsFragment newInstance(long receiptId, String query, long[] exludeIds) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);
        args.putString(ARG_SEARCH_QUERY, query);
        args.putLongArray(ARG_EXCLUDE_IDS, exludeIds);

        AddItemsFragment fragment = new AddItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = (OnItemAddedListener) getParentFragment();

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView list = getRecyclerView();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new ActionRecordTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID),
                args.getString(ARG_SEARCH_QUERY),
                args.getLongArray(ARG_EXCLUDE_IDS));
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        if(data == null)
            return;

        if(data.getCells().size() == 1){
            callback.onItemAdded(data.getCells().get(0).getId());
            getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        setListShown(true);
        setAdapter(new AddItemsReceiptAdapter(data.getCells()));
    }

    @Override
    public void onLoaderReset(Loader<TableViewModel> loader) {

    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){
        AddItemsReceiptAdapter adapter = (AddItemsReceiptAdapter) getAdapter();
        adapter.removeItem(event.getPosition());

        callback.onItemAdded(event.getRecordId());

        if(adapter.getItemCount() == 0){
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

//    @Override
//    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//        MenuInflater inflater = actionMode.getMenuInflater();
//        inflater.inflate(R.menu.menu_multiple_selection, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//        this.actionMode = actionMode;
//        return false;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//        if(menuItem.getItemId() == R.id.add){
//            returnRecords();
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public void onDestroyActionMode(ActionMode actionMode) {
//        ((RecordAdapterTest)getAdapter()).clearSelections();
//        this.actionMode = null;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(item.getItemId() == R.id.qr_scanner){
//            Intent intent =  new Intent(getActivity(), ReadFieldActvivity.class);
//
//            startActivityForResult(intent, REQUEST_QR_SCANNER);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void search(String query){
        getArguments().putString(ARG_SEARCH_QUERY, query);

        getLoaderManager().restartLoader(0, getArguments(), this);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == REQUEST_QR_SCANNER){
//            if(resultCode == CommonStatusCodes.SUCCESS){
//                if(data == null)
//                    return;
//
//                Barcode barcode = data.getParcelableExtra(BarcodeActivity.BarcodeObject);
//
//                submitQuery(barcode.displayValue);
//            }
//        }
//    }

//    @Override
//    public void onItemClick(ReceiptItemEvent event) {
//        RecordAdapterTest adapter = (RecordAdapterTest) getAdapter();
//
//        if(actionMode != null){
//            adapter.toggleSelection(event.getPosition());
//            if(adapter.getSelectedItemCount() == 0){
//                actionMode.finish();
//            }
//        }
//    }

//    @Subscribe
//    public void onLongItemClick(RecordLongClickEvent event){
//
//        if(actionMode != null)
//            return;
//
//        getActivity().startActionMode(this);
//        ((RecordAdapterTest)getAdapter()).toggleSelection(event.getPosition());
//    }

    private void returnRecords(){

        long[] checkedIds = ((RecordAdapterTest)getAdapter()).getSelectedItemIds();

        Intent data = new Intent();

        data.putExtra(DetailRecordActivity.EXTRA_RECORDS_ID, checkedIds);

        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
        getActivity().overridePendingTransition(com.android.ocasa.core.R.anim.activity_scale_in,
                com.android.ocasa.core.R.anim.activity_translatex_out);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_receipt_items, menu);
//
//    }
}
