package com.android.ocasa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.ocasa.R;
import com.android.ocasa.activity.CreateReceiptActivity;
import com.android.ocasa.activity.DetailReceiptActivity;
import com.android.ocasa.adapter.ReceiptAdapter;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.loader.ReceiptsTaskLoader;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Receipt;

import java.util.List;

/**
 * Ignacio Oviedo on 17/03/16.
 */
public class ReceiptListFragment extends BaseActionFragment{

    private ListView receiptList;
    private FloatingActionButton addButton;

    private boolean wasPaused = false;

    private ReceiptsCallback callback = new ReceiptsCallback();

    public static ReceiptListFragment newInstance(String actionId) {

        Bundle args = new Bundle();
        args.putString(ARG_ACTION_ID, actionId);

        ReceiptListFragment fragment = new ReceiptListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(1, getArguments(), callback);
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
    public void onResume() {
        super.onResume();

        if(wasPaused){
            getLoaderManager().restartLoader(1, getArguments(), callback);
            wasPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        wasPaused = true;
    }

    private void initControls(View view){
        receiptList = (ListView) view.findViewById(android.R.id.list);

        addButton = (FloatingActionButton) view.findViewById(R.id.add);
    }

    private void setListeners(){

        receiptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailReceiptActivity.class);
                intent.putExtra(DetailReceiptActivity.EXTRA_RECEIPT_ID, l);
                ((BaseActivity) getActivity()).startNewActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateReceiptActivity.class);
                intent.putExtra(CreateReceiptActivity.EXTRA_ACTION_ID, getArguments().getString(ARG_ACTION_ID));
                ((BaseActivity) getActivity()).startNewActivity(intent);
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<Action> loader, Action data) {
        super.onLoadFinished(loader, data);

        setTitle(data.getName());
    }

    private class ReceiptsCallback implements LoaderManager.LoaderCallbacks<List<Receipt>> {

        @Override
        public Loader<List<Receipt>> onCreateLoader(int id, Bundle args) {
            return new ReceiptsTaskLoader(getContext(), args.getString(ARG_ACTION_ID));
        }

        @Override
        public void onLoadFinished(Loader<List<Receipt>> loader, List<Receipt> data) {

            if (data == null)
                return;

            receiptList.setAdapter(new ReceiptAdapter(data));
        }

        @Override
        public void onLoaderReset(Loader<List<Receipt>> loader) {

        }
    }
}
