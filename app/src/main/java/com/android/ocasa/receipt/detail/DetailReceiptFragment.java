package com.android.ocasa.receipt.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewStub;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.adapter.DetailReceiptItemsAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.receipt.base.BaseReceiptFragment;
import com.android.ocasa.receipt.item.detail.DetailReceiptItemActivity;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ignacio on 07/07/16.
 */
public class DetailReceiptFragment extends BaseReceiptFragment {

    private TextView tableName;
    private RecyclerView list;

    public static DetailReceiptFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        DetailReceiptFragment fragment = new DetailReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadContent(ReceiptFormViewModel header) {
        ViewStub stub = (ViewStub) getView().findViewById(R.id.receipt_header_detail_action);

        if(stub == null)
            return;

        stub.inflate();

        tableName = (TextView) getView().findViewById(R.id.table_name);
        list = (RecyclerView) getView().findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .showLastDivider().build());

        getPresenter().items(header.getId());
    }

    @Override
    public void onItemsSuccess(TableViewModel table) {
        tableName.setText(table.getName());
        list.setAdapter(new DetailReceiptItemsAdapter(table.getCells()));
    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){
//        Intent intent = new Intent(getActivity(), DetailReceiptItemActivity.class);
//        intent.putExtra(DetailReceiptItemActivity.EXTRA_RECORD_ID, event.getRecordId());
//        intent.putExtra(DetailReceiptItemActivity.EXTRA_RECEIPT_ID, getArguments().getLong(ARG_RECEIPT_ID));
//        ((BaseActivity)getActivity()).startNewActivity(intent);
    }
}
