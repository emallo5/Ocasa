package com.android.ocasa.receipt.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;
import com.android.ocasa.event.CloseReceiptEvent;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.receipt.header.EditHeaderReceiptActivity;
import com.android.ocasa.adapter.ReceiptAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.receipt.base.BaseReceiptActivity;
import com.android.ocasa.receipt.detail.DetailReceiptActivity;
import com.android.ocasa.receipt.edit.EditReceiptActivity;
import com.android.ocasa.service.ReceiptService;
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
    private Button btnRefresh;
    private TextView tvCountDone;
    private TextView tvCountUndone;

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
        btnRefresh = (Button) view.findViewById(R.id.iv_refresh);
        tvCountDone = (TextView) view.findViewById(R.id.tv_pod_count_done);
        tvCountUndone = (TextView) view.findViewById(R.id.tv_pod_count_undone);
    }

    private void setListeners() {

        receiptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                ReceiptCellViewModel receiptViewModel = (ReceiptCellViewModel) receiptList.getItemAtPosition(i);

                Intent intent;

//                if (receiptViewModel.isOpen()) {
//                    intent = new Intent(getActivity(), EditReceiptActivity.class);
//                } else {
                    intent = new Intent(getActivity(), DetailReceiptActivity.class);
//                }

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

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
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
        updateCounters(table.getReceipts());
    }

    private void updateCounters (List<ReceiptCellViewModel> receipts) {
        int opened = 0;
        int closed = 0;

        for (ReceiptCellViewModel r : receipts) {
            if (r.isOpen())
                opened++;
            else
                closed++;
        }

        tvCountDone.setText(" : " + closed);
        tvCountUndone.setText(" : " + opened);
    }

    private void syncReceipts() {

        for (int i=0; i < ((ReceiptAdapter) receiptList.getAdapter()).getReceipts().size(); i++) {

            final ReceiptCellViewModel receipt = ((ReceiptAdapter) receiptList.getAdapter()).getReceipts().get(i);
            if (receipt.isOpen()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPresenter().close(receipt.getId());
                        }
                    }, i * 1000);
//                }
            }
        }
    }

    @Override
    public void onCloseReceiptSuccess() {
        hideProgress();
        getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    public void showProgress() {
        ProgressDialogFragment.newInstance("Sincronizando...").show(getChildFragmentManager(), "Progress");
    }

    public void hideProgress() {
        DialogFragment dialog = (DialogFragment) getChildFragmentManager().findFragmentByTag("Progress");
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public void onPosiviteClick(String tag) {

        ReceiptService service = new ReceiptService();
        Receipt receipt = service.findReceiptById(getContext(), receiptId);

        if (receipt.isOpen()) {
            getPresenter().close(receiptId);
            showProgress();
        } else
            getPresenter().receipts(getArguments().getString(ARG_ACTION_ID));
    }

    @Override
    public void onNeutralClick(String tag) {

    }

    @Override
    public void onNegativeClick(String tag) {

    }
}
