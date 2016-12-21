package com.android.ocasa.receipt.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.event.CloseReceiptEvent;
import com.android.ocasa.receipt.header.EditHeaderReceiptActivity;
import com.android.ocasa.adapter.ReceiptAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.receipt.base.BaseReceiptActivity;
import com.android.ocasa.receipt.detail.DetailReceiptActivity;
import com.android.ocasa.receipt.edit.EditReceiptActivity;
import com.android.ocasa.util.AlertDialogFragment;
import com.android.ocasa.util.ProgressDialogFragment;
import com.android.ocasa.viewmodel.ReceiptCellViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.codika.androidmvp.fragment.BaseMvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 11/07/16.
 */
public class ReceiptListFragment extends BaseMvpFragment<ReceiptListView, ReceiptListPresenter> implements ReceiptListView,
AlertDialogFragment.OnAlertClickListener{

    static final String ARG_ACTION_ID = "action_id";

    private ListView receiptList;
    private FloatingActionButton addButton;
    private ArrayList<Long> uploadingIds = new ArrayList<>();

    public static ReceiptListFragment newInstance(String actionId) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        ReceiptListFragment fragment = new ReceiptListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private long receiptId;

    @Subscribe
    public void onCloseReceipt(CloseReceiptEvent event){
        receiptId = event.getReceiptId();

        AlertDialogFragment.newInstance("Contabilizar","¿Esta seguro de contabilizar el comprobante? No podra volver a modificarlo").show(getChildFragmentManager(), "CloseConfirmation");

    }

    private void initControls(View view){
        receiptList = (ListView) view.findViewById(android.R.id.list);

        addButton = (FloatingActionButton) view.findViewById(R.id.add);
    }

    private void setListeners(){

        receiptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ReceiptCellViewModel receiptViewModel = (ReceiptCellViewModel) receiptList.getItemAtPosition(i);

                Intent intent;

                if(receiptViewModel.isOpen()){
                    intent = new Intent(getActivity(), EditReceiptActivity.class);
                }else{
                    intent = new Intent(getActivity(), DetailReceiptActivity.class);
                }

                intent.putExtra(BaseReceiptActivity.EXTRA_RECEIPT_ID, l);
                ((BaseActivity) getActivity()).startNewActivity(intent);

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditHeaderReceiptActivity.class);
                intent.putExtra(EditHeaderReceiptActivity.EXTRA_ACTION_ID, getArguments().getString(ARG_ACTION_ID));
                ((BaseActivity) getActivity()).startNewActivity(intent);
            }
        });
    }

    public void setTitle(String title){
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public ReceiptListView getMvpView() {
        return this;
    }

    @Override
    public Loader<ReceiptListPresenter> getPresenterLoader() {
        return new ReceiptListLoader(getActivity());
    }

    @Override
    public void onReceiptsLoadSuccess (ReceiptTableViewModel table) {
        if (table == null)
            return;

        setTitle("Listado " + table.getName());

        receiptList.setAdapter(new ReceiptAdapter(table.getReceipts()));

        syncReceipts();
    }

    private void syncReceipts() {

        for (int i=0; i<((ReceiptAdapter) receiptList.getAdapter()).getReceipts().size(); i++) {

            ReceiptCellViewModel receipt = ((ReceiptAdapter) receiptList.getAdapter()).getReceipts().get(i);
            if (receipt.isOpen()) {
                if (!uploadingIds.contains(receipt.getId())) {
                    showProgress();
                    uploadingIds.add(receipt.getId());
                    getPresenter().close(receipt.getId());
                }
            }
        }
    }

    @Override
    public void onCloseReceiptSuccess() {
        hideProgress();
        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    public void showProgress() {
        ProgressDialogFragment.newInstance("Guardando...").show(getChildFragmentManager(), "Progress");
    }

    public void hideProgress() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if(dialog != null) dialog.dismiss();
    }

    @Override
    public void onPosiviteClick(String tag) {
        showProgress();
        uploadingIds.add(receiptId);
        getPresenter().close(receiptId);
    }

    @Override
    public void onNeutralClick(String tag) {

    }

    @Override
    public void onNegativeClick(String tag) {

    }
}
