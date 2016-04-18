package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.loader.ReceiptTaskLoaderTest;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;

import java.util.Date;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class DetailHeaderReceiptFragment extends BaseReceiptFragment{

    private TextView dateView;
    private TextView timeView;

    private LinearLayout container;

    public static DetailHeaderReceiptFragment newInstance(long receiptId) {

        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        DetailHeaderReceiptFragment fragment = new DetailHeaderReceiptFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        dateView = (TextView) view.findViewById(R.id.date);

        timeView = (TextView) view.findViewById(R.id.time);

        container = (LinearLayout) view.findViewById(R.id.container);
    }

    @Override
    public void onLoadFinished(Loader<Receipt> loader, Receipt data) {
        super.onLoadFinished(loader, data);

        setTitle(data.getAction().getName());

        Date validityDate = DateTimeHelper.parseDateTime(data.getValidityDate());

        dateView.setText(DateTimeHelper.formatDate(validityDate));
        timeView.setText(DateTimeHelper.formatTime(validityDate));
    }

    private void fillFields(FormViewModel form){

        if(container.getChildCount() > 2)
            return;

        for (FieldViewModel field : form.getFields()){

            FieldViewFactory factory = field.getType().getFieldFactory();

            View view = factory.createView(container, field, false);
            view.setTag(field.getTag());

            container.addView(view);
        }
    }

    private class FormViewModelCallback implements LoaderManager.LoaderCallbacks<FormViewModel>{

        @Override
        public Loader<FormViewModel> onCreateLoader(int id, Bundle args) {
            return new ReceiptTaskLoaderTest(getActivity(), args.getLong(ARG_RECEIPT_ID));
        }

        @Override
        public void onLoadFinished(Loader<FormViewModel> loader, FormViewModel data) {
            fillFields(data);
        }

        @Override
        public void onLoaderReset(Loader<FormViewModel> loader) {}

    }
}
