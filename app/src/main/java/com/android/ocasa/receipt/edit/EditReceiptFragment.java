package com.android.ocasa.receipt.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.adapter.RecieptPagerAdapter;
import com.android.ocasa.fragment.AddItemsFragment;
import com.android.ocasa.receipt.item.available.AvailableItemsFragment;
import com.android.ocasa.receipt.item.detailaction.DetailActionActivity;
import com.android.ocasa.receipt.item.detailaction.DetailActionFragment;
import com.android.ocasa.receipt.item.list.ReceiptItemsFragment;
import com.android.ocasa.pickup.scanner.ScannerActivity;
import com.android.ocasa.pickup.util.PickupItemConfirmationDialog;
import com.android.ocasa.receipt.base.BaseReceiptFragment;
import com.android.ocasa.receipt.base.BaseReceiptPresenter;
import com.android.ocasa.receipt.base.BaseReceiptView;
import com.android.ocasa.service.TableService;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class EditReceiptFragment extends BaseReceiptFragment implements EditReceiptView,
        OnItemChangeListener, PickupItemConfirmationDialog.OnConfirmationListener,
        AlertDialogFragment.OnAlertClickListener {

    static final String TAG = "EditReceiptFragment";
    static final int GPS_REQUEST = 100;

    static final int SCANNER_REQUEST_CODE = 1000;
    static final int DETAIL_ACTION_CODE = 1001;

    public static final String RECORD_DATA = "record_data";

    public boolean readyToExit = false;

    private ImageView glass;
    private EditText search;
    private ImageView clearSearch;
    private TabLayout tabs;
    private ViewPager pager;
    private ImageView scanner;
    private CardView searchResultsContainer;

    private String codeNotFound;

    private long[] recordIds;
    private CellViewModel currentRecordEditing = null;

    private MediaPlayer checkSound;
    private MediaPlayer errorSound;

    public static EditReceiptFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        EditReceiptFragment fragment = new EditReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        recordIds = new long[0];

        checkSound = MediaPlayer.create(getActivity(), R.raw.check_in_sound);
        errorSound = MediaPlayer.create(getActivity(), R.raw.error_sound);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return;

        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, GPS_REQUEST);
            return;
        }

        final Activity thisActivity = getActivity();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        GPS_REQUEST);
            }
        };

        Snackbar.make(content, "Habilite el GPS por favor",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", listener)
                .show();
    }

    @Override
    public void onLoadContent(ReceiptFormViewModel header) {
        ViewStub stub = (ViewStub) getView().findViewById(R.id.receipt_header_edit_action);

        if(stub == null){
            return;
        }

        stub.inflate();

        glass = (ImageView) getView().findViewById(R.id.glass);

        search = (EditText) getView().findViewById(R.id.search);

        clearSearch = (ImageView) getView().findViewById(R.id.clear_search);

        scanner = (ImageView) getView().findViewById(R.id.scanner);

        searchResultsContainer = (CardView) getView().findViewById(R.id.sub_container);

        tabs = (TabLayout) getView().findViewById(R.id.receipt_items_tabs);
        pager = (ViewPager) getView().findViewById(R.id.receipt_items_pager);

        RecieptPagerAdapter adapter = new RecieptPagerAdapter(getChildFragmentManager(),
                getResources().getStringArray(R.array.create_receipt_tabs));
        adapter.addFragment(AvailableItemsFragment.newInstance(getArguments().getLong(ARG_RECEIPT_ID)));
        adapter.addFragment(ReceiptItemsFragment.newInstance(getArguments().getLong(ARG_RECEIPT_ID)));

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        setListeners();
    }

    private void setListeners(){

        RxTextView.textChanges(search)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {

                        if (charSequence.toString().isEmpty()) return;

                        RecieptPagerAdapter adapter = (RecieptPagerAdapter) pager.getAdapter();
                        AvailableItemsFragment availFrag = (AvailableItemsFragment) adapter.getItem(0);
                        availFrag.filterItems(charSequence.toString());

//                    openFromScanner(charSequence); este codigo estaba antes
                    }
                });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search.setText("");

                AddItemsFragment frag = (AddItemsFragment) getChildFragmentManager().findFragmentByTag("SearchItems");

                if(frag != null) {
                    getChildFragmentManager()
                            .beginTransaction()
                            .remove(frag)
                            .commit();
                }
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), ScannerActivity.class), SCANNER_REQUEST_CODE);
            }
        });

        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1, true);
            }
        });
    }

    public void setTitleFromChild(int total, int pending) {
        if (getActivity() != null)
            ((EditReceiptActivity) getActivity()).setCounters(total, pending);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == DETAIL_ACTION_CODE) {

            long itemId = data.getExtras().getLong(RECORD_DATA);

            if (currentRecordEditing == null || currentRecordEditing.getId() != itemId)
                return;

            recordIds = ArrayUtils.add(recordIds, itemId);

            searchResultsContainer.setVisibility(View.GONE);

            for (FieldViewModel field : currentRecordEditing.getFields()) {
                if (field.isEditable()) {
                    field.setValue(data.getExtras().getString(field.getLabel()));
                }
            }

            RecieptPagerAdapter adapter = (RecieptPagerAdapter) pager.getAdapter();
            ReceiptItemsFragment receiptFrag = (ReceiptItemsFragment) adapter.getItem(1);
            receiptFrag.addItem(currentRecordEditing);

            AvailableItemsFragment availFrag = (AvailableItemsFragment) adapter.getItem(0);
            availFrag.removeitem(itemId);

            currentRecordEditing = null;

            if (data.getExtras().getBoolean(DetailActionFragment.EXIT_POD))
                readyToExit = true;
        }

        if(requestCode == SCANNER_REQUEST_CODE) {
            // *** saco el codigo que busca los resultados del lector de barras y lo cambio por codigo que pone
            // *** el unico valor leido !

//            long[] codes = data.getLongArrayExtra(ScannerActivity.EXTRA_RESULT_CODES);
//            long[] newCodes = new long[0];
//
//            for (long code : codes){
//                if(!ArrayUtils.contains(recordIds, code)) {
//                    recordIds = ArrayUtils.add(recordIds, code);
//                    newCodes = ArrayUtils.add(newCodes, code);
//                }
//            }
//            ((EditReceiptPresenter)getPresenter()).findItems(getArguments().getLong(ARG_RECEIPT_ID), newCodes);

//            search.setText(data.getStringExtra(ScannerActivity.EXTRA_RESULT_CODES)); este es para el search
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openFromScanner(data.getStringExtra(ScannerActivity.EXTRA_RESULT_CODES));
                }
            }, 1300);
        }
    }

    private void openFromScanner (CharSequence charSequence) {

        if (charSequence == null) return;

        AddItemsFragment frag = (AddItemsFragment) getChildFragmentManager().findFragmentByTag("SearchItems");

        if (frag == null && charSequence.length() == 0) {
            searchResultsContainer.setVisibility(View.GONE);
            return;
        }

        if (frag == null) {
            AddItemsFragment fragment = AddItemsFragment.newInstance(getArguments().getLong(ARG_RECEIPT_ID), charSequence.toString().trim(), recordIds);
            getChildFragmentManager().beginTransaction().replace(R.id.sub_container, fragment, "SearchItems").commit();

//            searchResultsContainer.setVisibility(View.VISIBLE);
        } else {
            if (charSequence.length() == 0) {
                searchResultsContainer.setVisibility(View.GONE);
                getChildFragmentManager()
                        .beginTransaction()
                        .remove(frag)
                        .commit();
                return;
            }

//            searchResultsContainer.setVisibility(View.VISIBLE); Porque ya no mostramos los resultados aparte, los filtramos en la lista misma
            frag.search(charSequence.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (readyToExit) {
            readyToExit = false;
            save(false);
        }
    }

    @Override
    public void onItemsSuccess(TableViewModel table) {
        if(recordIds.length > 0)
            return;

        recordIds = new long[table.getCells().size()];

        for (int index = 0; index < table.getCells().size(); index++){
            CellViewModel cell = table.getCells().get(index);
            recordIds[index] = cell.getId();
        }

        RecieptPagerAdapter adapter = (RecieptPagerAdapter) pager.getAdapter();
        ReceiptItemsFragment frag = (ReceiptItemsFragment) adapter.getItem(1);
        frag.addItems(table.getCells());
    }

    @Override
    public BaseReceiptView getMvpView() {
        return this;
    }

    @Override
    public Loader<BaseReceiptPresenter> getPresenterLoader() {
        return new EditReceiptLoader(getActivity());
    }

    @Override
    public void onItemsFoundSuccess(List<CellViewModel> items) {
        if(items != null && !items.isEmpty()) {
            RecieptPagerAdapter adapter = (RecieptPagerAdapter) pager.getAdapter();
            ReceiptItemsFragment frag = (ReceiptItemsFragment) adapter.getItem(1);
            frag.addItems(items);

            search.setText("");
            search.requestFocus();
            checkSound.start();
        }
    }

    @Override
    public void onReceiptItemsEmpty() {
        Toast.makeText(getActivity(), "Debe cargar algun Item", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiptSaveSuccess() {
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    @Override
    public void showProgress() {
        showProgressCustom("Enviando...");
    }

    public void showProgressCustom(String message) {
        ProgressDialogFragment pdf = ProgressDialogFragment.newInstance(message);
        pdf.setCancelable(false);
        pdf.show(getChildFragmentManager(), "Progress");
    }

    @Override
    public void hideProgress() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if(dialog != null) dialog.dismiss();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onAdd(String code) {

    }

    public long[] getRecordIds() {
        return recordIds;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save){
            showSaveAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSaveAlert() {
//        AlertDialogFragment.newInstance("Guardar","¿Desea guardar los cambios?", "Guardar", null, "Contabilizar")
//                .show(getChildFragmentManager(), "SaveConfirmation");

        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("Guardar", "¿Desea guardar los cambios?");
        dialog.show(getChildFragmentManager(), "SaveConfirmation");
    }

    @Override
    public void onPosiviteClick(String tag) {

        if(tag.equalsIgnoreCase("createRecord")){
            CellViewModel cell = new TableService().addRecordToTable(getContext(), codeNotFound);
            onItemAdded(cell);
            return;
        }

        if(tag.equalsIgnoreCase("Dialog")){
            getActivity().finish();
            return;
        }

        save(tag.equalsIgnoreCase("CloseConfirmation"));
    }

    @Override
    public void onNeutralClick(String tag) {
        AlertDialogFragment.newInstance("Contabilizar","¿Esta seguro de contabilizar el comprobante? No podra volver a modificarlo").show(getChildFragmentManager(), "CloseConfirmation");
    }

    @Override
    public void onNegativeClick(String tag) {

    }

    private void save(boolean close) {
        FormViewModel receiptHeader = getReceiptHeader();
        ((EditReceiptPresenter)getPresenter()).saveReceipt(receiptHeader.getId(), recordIds, getLastLocation(), close);
    }

    @Override
    public void onShowSearchResults() {
        search.requestFocus();
    }

    @Override
    public void onItemAdded(CellViewModel item) {

        if(ArrayUtils.contains(recordIds, item.getId())){
            return;
        }

        search.setText("");
        search.requestFocus();
        checkSound.start();

        currentRecordEditing = item;
        goDetailActionScreen(item.getId());
        //((EditReceiptPresenter)getPresenter()).checkDetailFields(getArguments().getLong(ARG_RECEIPT_ID), item.getId());
//        ((EditReceiptPresenter)getPresenter()).findItem(getArguments().getLong(ARG_RECEIPT_ID), item.getId());
    }

    private void goDetailActionScreen(long recordId) {
        Intent intent = new Intent(getActivity(), DetailActionActivity.class);
        intent.putExtra(DetailActionActivity.EXTRA_RECEIPT_ID, getArguments().getLong(ARG_RECEIPT_ID));
        intent.putExtra(DetailActionActivity.EXTRA_RECORD_ID, recordId);
        intent.putExtra(DetailActionActivity.POD_TYPE_MASIVE, currentRecordEditing.getColumnValue("OM_MOVILNOVEDAD_C_0014").equalsIgnoreCase("M")); // TODO: preguntar si el motivo es "M" !!!
        startActivityForResult(intent, DETAIL_ACTION_CODE);
    }

    @Override
    public void onItemRemoved(CellViewModel item) {
        Log.v(TAG, "Receipt item removed " + item.getId());

        recordIds = ArrayUtils.removeElement(recordIds, item.getId());

        RecieptPagerAdapter adapter = (RecieptPagerAdapter) pager.getAdapter();
        AvailableItemsFragment frag = (AvailableItemsFragment) adapter.getItem(0);
        frag.addItem(item);
    }

    @Override
    public void onItemNotFound(String code) {
        search.setText("");
        search.requestFocus();
        errorSound.start();
//        PickupItemConfirmationDialog.newInstance(code).show(getChildFragmentManager(), "ConfirmationDialog");
        onCreateItem(code);
    }

    @Override
    public void onCreateItem(String id) {
        codeNotFound = id;
        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("Pieza no econtrada", "¿Desea agregarla?");
        dialog.show(getChildFragmentManager(), "createRecord");
    }

    @Override
    public void onPreviousItemsLoaded(long[] recordIds) {
        this.recordIds = ArrayUtils.addAll(this.recordIds, recordIds);
    }

}
