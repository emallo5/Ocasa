package com.android.ocasa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.ocasa.R;
import com.android.ocasa.activity.DetailReceiptItemActivity;
import com.android.ocasa.adapter.ReceiptItemsDetailAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.event.ReceiptItemEvent;
import com.android.ocasa.loader.ActionTaskLoaderTest;
import com.android.ocasa.loader.ReceiptRecordsTaskLoader;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;
import com.android.ocasa.widget.factory.FieldViewFactory;
import com.google.android.gms.vision.text.Line;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Ignacio Oviedo on 28/03/16.
 */
public class DetailReceiptItemsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<TableViewModel>{

    static final String ARG_RECEIPT_ID = "receipt_id";

    private LinearLayout container;
    private RecyclerView list;

    public static DetailReceiptItemsFragment newInstance(long receiptId) {
        
        Bundle args = new Bundle();
        args.putLong(ARG_RECEIPT_ID, receiptId);

        DetailReceiptItemsFragment fragment = new DetailReceiptItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(1, getArguments(), new FormViewModelCallback());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_items, container, false);
    }

   @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       container = (LinearLayout) view.findViewById(R.id.container);

//       list = (RecyclerView) view.findViewById(R.id.list);
//       list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//       list.setHasFixedSize(true);
//       list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//               .showLastDivider().build());

//        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add);
//        add.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onItemClick(ReceiptItemEvent event){
        Intent intent = new Intent(getActivity(), DetailReceiptItemActivity.class);
        intent.putExtra(DetailReceiptItemActivity.EXTRA_RECORD_ID, event.getRecordId());
        intent.putExtra(DetailReceiptItemActivity.EXTRA_RECEIPT_ID, getArguments().getLong(ARG_RECEIPT_ID));
        ((BaseActivity)getActivity()).startNewActivity(intent);
    }

    private void fillFields(FormViewModel form){

        setTitle(form.getTitle());

        FieldViewFactory factory = FieldType.TEXT.getFieldFactory();

        if(container.getChildCount() > 2)
            return;

        for (FieldViewModel field : form.getFields()){

            View view = factory.createView(container, field, true);
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

    @Override
    public Loader<TableViewModel> onCreateLoader(int id, Bundle args) {
        return new ReceiptRecordsTaskLoader(getContext(), args.getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void onLoadFinished(Loader<TableViewModel> loader, TableViewModel data) {
        if(!data.getCells().isEmpty()){
            list.setAdapter(new ReceiptItemsDetailAdapter(data.getCells()));
        }
    }

    @Override
    public void onLoaderReset(Loader<TableViewModel> loader) {

    }
}
