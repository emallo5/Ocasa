package com.android.ocasa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.activity.DetailRecordActivity;
import com.android.ocasa.adapter.RecieptPagerAdapter;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.loader.ActionTaskLoaderTest;
import com.android.ocasa.loader.RecordsTaskLoaderTest;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.pickup.scanner.ScannerActivity;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Ignacio Oviedo on 04/04/16.
 */
public class CreateHeaderReceiptFragment extends LocationFragment implements
        AddItemsFragment.OnItemAddedListener, ReceiptItemsFragment.OnAddItemsListener,
        AlertDialogFragment.OnAlertClickListener, AvailableItemsFragment.OnTableLoad{

    static final int SCANNER_REQUEST_CODE = 1000;

    static final String ARG_RECEIPT_ID = "receipt_id";

    private LinearLayout container;

    private SearchView searchView;
    private TabLayout tabs;
    private ViewPager pager;
    private ImageView scanner;
    private TextView showTable;
    private CardView searchResultsContainer;

    private RecordsLoaderCallback callback = new RecordsLoaderCallback();

    private long[] recordIds;

    private FormViewModel receiptHeader;

    public static CreateHeaderReceiptFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        CreateHeaderReceiptFragment fragment = new CreateHeaderReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordIds = new long[0];

        setHasOptionsMenu(true);

        getLoaderManager().initLoader(1, getArguments(), new FormViewModelCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_receipt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = (LinearLayout) view.findViewById(R.id.container);

        searchView = (SearchView) view.findViewById(R.id.search);

        scanner = (ImageView) view.findViewById(R.id.scanner);

        searchResultsContainer = (CardView) view.findViewById(R.id.sub_container);

        showTable = (TextView) view.findViewById(R.id.show_table);

        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        pager = (ViewPager) view.findViewById(R.id.pager);

        RecieptPagerAdapter adapter = new RecieptPagerAdapter(getChildFragmentManager(),
                getResources().getStringArray(R.array.create_receipt_tabs));
        adapter.addFragment(ReceiptItemsFragment.newInstance());
        adapter.addFragment(AvailableItemsFragment.newInstance(getArguments().getLong(ARG_RECEIPT_ID)));

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            showAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        showAlert();
    }

    private void showAlert() {
        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("Borrar " + receiptHeader.getTitle(), "¿Desea descartar los cambios?");

        dialog.show(getChildFragmentManager(), "Dialog");
    }

    private void setListeners(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AddItemsFragment frag = (AddItemsFragment) getChildFragmentManager().findFragmentByTag("SearchItems");

                if(frag == null) {

                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.sub_container, AddItemsFragment.newInstance(getArguments().getLong(ARG_RECEIPT_ID), newText, recordIds), "SearchItems")
                            .commit();
                }else{
                    if(newText.isEmpty()) {
                        searchResultsContainer.setVisibility(View.GONE);

                        getChildFragmentManager()
                                .beginTransaction()
                                .remove(frag)
                                .commit();

                        return true;
                    }

                    searchResultsContainer.setVisibility(View.VISIBLE);

                    frag.search(newText);
                }

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                AddItemsFragment frag = (AddItemsFragment) getChildFragmentManager().findFragmentByTag("SearchItems");

                if(frag != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .remove(frag)
                            .commit();
                }

                return true;
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ScannerActivity.class), SCANNER_REQUEST_CODE);
            }
        });

        showTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1, true);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SCANNER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                long[] codes = data.getLongArrayExtra(ScannerActivity.EXTRA_RESULT_CODES);

                long[] newCodes = new long[0];

                for (long code : codes){
                    if(!ArrayUtils.contains(recordIds, code)) {
                        recordIds = ArrayUtils.add(recordIds, code);
                        newCodes = ArrayUtils.add(newCodes, code);
                    }
                }

                Bundle args = new Bundle();
                args.putLongArray(DetailRecordActivity.EXTRA_RECORDS_ID, newCodes);
                args.putLong(ARG_RECEIPT_ID, getArguments().getLong(ARG_RECEIPT_ID));

                if(getLoaderManager().getLoader(2) == null)
                    getLoaderManager().initLoader(2, args, callback);
                else
                    getLoaderManager().restartLoader(2, args, callback);

            }
        }
    }

    private void fillFields(FormViewModel form){

        receiptHeader = form;

        setTitle(form.getTitle());

        FieldViewFactory factory = FieldType.TEXT.getFieldFactory();

        if(container.getChildCount() > 2)
            return;

        for (FieldViewModel field : form.getFields()){

            View view = factory.createView(container, field, true);
            view.setTag(field.getTag());

            container.addView(view);
        }
    }

    @Override
    public void onItemAdded(long recordId) {
        if(ArrayUtils.contains(recordIds, recordId)){
            return;
        }

        if(pager.getCurrentItem() == 1){
            pager.setCurrentItem(0, true);
        }

        recordIds = ArrayUtils.add(recordIds, recordId);

        Bundle args = new Bundle();
        args.putLongArray(DetailRecordActivity.EXTRA_RECORDS_ID, ArrayUtils.toPrimitive(ArrayUtils.toArray(recordId)));
        args.putLong(ARG_RECEIPT_ID, getArguments().getLong(ARG_RECEIPT_ID));

        if(getLoaderManager().getLoader(2) == null)
            getLoaderManager().initLoader(2, args, callback);
        else
            getLoaderManager().restartLoader(2, args, callback);

        AddItemsFragment frag = (AddItemsFragment) getChildFragmentManager().findFragmentByTag("SearchItems");

        if(frag != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .remove(frag)
                    .commit();
        }

        searchResultsContainer.setVisibility(View.GONE);
    }

    public FormViewModel getReceiptHeader(){
        return receiptHeader;
    }

    public long[] getRecordIds(){
        return recordIds;
    }

    @Override
    public void onItemRemoved(long recordId) {
        recordIds = ArrayUtils.removeElement(recordIds, recordId);
    }

    @Override
    public void onPosiviteClick() {
        new ReceiptDAO(getActivity()).delete(getArguments().getLong(ARG_RECEIPT_ID));

        NavUtils.navigateUpFromSameTask(getActivity());
    }

    @Override
    public void onNegativeClick() {

    }

    @Override
    public void onLoad(String tableName) {
        showTable.setText(tableName);
    }

    private class FormViewModelCallback implements LoaderManager.LoaderCallbacks<FormViewModel>{

        @Override
        public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
            return new ActionTaskLoaderTest(getActivity(), args.getLong(ARG_RECEIPT_ID));
        }

        @Override
        public void onLoadFinished(Loader<FormViewModel> loader, FormViewModel data) {
            fillFields(data);
        }

        @Override
        public void onLoaderReset(Loader<FormViewModel> loader) {}

    }

    public class RecordsLoaderCallback implements LoaderManager.LoaderCallbacks<List<CellViewModel>>{

        @Override
        public Loader<List<CellViewModel>> onCreateLoader(int id, Bundle args) {
            return new RecordsTaskLoaderTest(getActivity(), args.getLong(ARG_RECEIPT_ID),
                    args.getLongArray(DetailRecordActivity.EXTRA_RECORDS_ID));
        }

        @Override
        public void onLoadFinished(Loader<List<CellViewModel>> loader, List<CellViewModel> data) {

            if(data != null && !data.isEmpty()) {
                RecieptPagerAdapter adapter = (RecieptPagerAdapter) pager.getAdapter();
                ReceiptItemsFragment frag = (ReceiptItemsFragment) adapter.getItem(0);
                frag.addItems(data);

                searchView.setQuery(data.get(0).getKeyValue(), false);
                searchView.setIconified(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<CellViewModel>> loader) {

        }
    }
}

