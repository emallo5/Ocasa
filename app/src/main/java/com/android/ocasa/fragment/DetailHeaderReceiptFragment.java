package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.adapter.DetailReceiptItemsAdapter;
import com.android.ocasa.loader.ActionTaskLoaderTest;
import com.android.ocasa.loader.ReceiptRecordsTaskLoader;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class DetailHeaderReceiptFragment extends BaseReceiptFragment{

    private LinearLayout container;
    private TextView tableName;
    private RecyclerView list;

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
        getLoaderManager().initLoader(2, getArguments(), new TableViewModelCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_receipt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container = (LinearLayout) view.findViewById(R.id.container);
        tableName = (TextView) view.findViewById(R.id.table_name);
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .showLastDivider().build());
    }

    @Override
    public void onLoadFinished(Loader<Receipt> loader, Receipt data) {
        super.onLoadFinished(loader, data);

        setTitle(data.getAction().getName());
    }

    private void fillFields(FormViewModel form){

        if(container.getChildCount() > 2)
            return;

        for (FieldViewModel field : form.getFields()){

            FieldViewFactory factory = FieldType.TEXT.getFieldFactory();

            View view = factory.createView(container, field, false);
            view.setTag(field.getTag());

            container.addView(view);
        }
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

    private class TableViewModelCallback implements LoaderManager.LoaderCallbacks<TableViewModel>{

        @Override
        public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
            return new ReceiptRecordsTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID));
        }

        @Override
        public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
            tableName.setText(data.getName());
            list.setAdapter(new DetailReceiptItemsAdapter(data.getCells()));
        }

        @Override
        public void onLoaderReset(Loader<TableViewModel> loader) {}

    }
}
