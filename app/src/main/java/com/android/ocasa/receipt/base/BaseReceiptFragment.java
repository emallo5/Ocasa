package com.android.ocasa.receipt.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.ocasa.R;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.LocationMvpFragment;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;

/**
 * Created by ignacio on 07/07/16.
 */
public abstract class BaseReceiptFragment extends LocationMvpFragment<BaseReceiptView, BaseReceiptPresenter> implements BaseReceiptView{

    public static final String ARG_RECEIPT_ID = "receipt_id";

    private CardView headerFormContainer;
    private LinearLayout headerContainer;
    protected RelativeLayout content;

    private ReceiptFormViewModel header;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initControls(view);
        content = (RelativeLayout) view.findViewById(R.id.content);
    }

    private void initControls(View view){
        headerFormContainer = (CardView) view.findViewById(R.id.receipt_header_form);
        headerContainer = (LinearLayout) view.findViewById(R.id.receipt_header_container);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().header(getArguments().getLong(ARG_RECEIPT_ID));
    }

    @Override
    public BaseReceiptView getMvpView() {
        return this;
    }

    @Override
    public Loader<BaseReceiptPresenter> getPresenterLoader() {
        return new BaseReceiptLoader(getActivity());
    }

    @Override
    public void onHeaderSuccess(ReceiptFormViewModel header) {

        headerFormContainer.setCardBackgroundColor(Color.parseColor("#ffffff"/*header.getColor()*/));

        this.header = header;

        setTitle(header.getTitle());

        FieldViewFactory factory = FieldType.TEXT.getFieldFactory();

        if(headerContainer.getChildCount() > 0)
            headerContainer.removeAllViewsInLayout();

        for (FieldViewModel field : header.getFields()){

            View view = factory.createView(headerContainer, field, true);
            view.setTag(field.getTag());

            headerContainer.addView(view);
        }

        onLoadContent(header);
    }

    public void setTitle(String title){
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    public String getTitle() {
        return ((BaseActivity) getActivity()).getSupportActionBar().getTitle().toString();
    }

    public abstract void onLoadContent(ReceiptFormViewModel header);

    public FormViewModel getReceiptHeader() {
        return header;
    }
}
