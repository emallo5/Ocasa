package com.android.ocasa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class ReceiptAdapter extends BaseAdapter {

    private List<CellViewModel> receipts;

    private int fieldCount;

    public ReceiptAdapter(List<CellViewModel> receipts) {
        this.receipts = receipts;
        fieldCount = receipts.isEmpty() ? 0 : receipts.get(0).getFields().size();
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int i) {
        return receipts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return receipts.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        RecordHolder holder;

        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_receipt_header, viewGroup, false);

            holder = new RecordHolder(view, fieldCount);

            view.setTag(holder);
        }else{
            holder = (RecordHolder) view.getTag();
        }

        CellViewModel receipt = receipts.get(position);

        List<FieldViewModel> fields = receipt.getFields();

        for (int index = 0; index < fields.size(); index++){
            holder.views.get(index).setText(fields.get(index).getValue());
        }

        return view;
    }

    static class RecordHolder{

        ArrayList<TextView> views;

        public RecordHolder(View view, int fieldCount){

            views = new ArrayList<>();

            LinearLayout container = (LinearLayout) view.findViewById(R.id.container);

            for (int index = 0; index < fieldCount; index++){
                TextView text = new TextView(view.getContext());
                views.add(text);
                container.addView(text);
            }

        }

    }
}
