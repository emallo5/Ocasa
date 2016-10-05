package com.android.ocasa.record.invoke;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.ocasa.core.FilterTableFragment;
import com.android.ocasa.event.ReceiptItemEvent;

/**
 * Created by ignacio on 14/07/16.
 */
public class InvokeRecordFragment extends FilterTableFragment {

    public static InvokeRecordFragment newInstance(String tableId) {

        Bundle args = new Bundle();
        args.putString(ARG_LAYOUT_ID, tableId);

        InvokeRecordFragment fragment = new InvokeRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRecyclerView().setBackgroundColor(Color.parseColor("#9E9E9E"));

        getAction().setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(ReceiptItemEvent event) {

        Intent result = new Intent();
        result.putExtra("record_id", event.getRecordId());

        getActivity().setResult(Activity.RESULT_OK, result);
        getActivity().finish();
    }
}
