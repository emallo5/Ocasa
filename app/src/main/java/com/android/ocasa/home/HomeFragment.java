package com.android.ocasa.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.ocasa.R;
import com.android.ocasa.core.FilterTableFragment;
import com.android.ocasa.record.create.CreateRecordActivity;
import com.android.ocasa.record.detail.DetailRecordActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.viewmodel.TableViewModel;

/**
 * Ignaco Oviedo on 11/01/16.
 */
public class HomeFragment extends FilterTableFragment {

    public static HomeFragment newInstance(String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_TABLE_ID, tableId);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getAction().setImageResource(R.drawable.ic_content_add);
        getAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateRecordActivity.class);
                intent.putExtra(CreateRecordActivity.EXTRA_TABLE_ID, getArguments().getString(ARG_TABLE_ID));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(ReceiptItemEvent event) {
        super.onItemClick(event);

        Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
        intent.putExtra(DetailRecordActivity.EXTRA_RECORD_ID, event.getRecordId());
        ((BaseActivity) getActivity()).startNewActivity(intent);
    }

    @Override
    public void onTableLoadSuccess(TableViewModel table) {
        super.onTableLoadSuccess(table);

        if(table != null){
            ((BaseActivity)getActivity()).getDelegate().getSupportActionBar().setTitle(table.getName());
            getRecyclerView().setBackgroundColor(Color.parseColor(table.getColor()));

        }
    }
}
