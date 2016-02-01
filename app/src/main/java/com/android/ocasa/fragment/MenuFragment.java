package com.android.ocasa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.ocasa.R;
import com.android.ocasa.adapter.MenuAdapter;
import com.android.ocasa.barcode.BarcodeActivity;
import com.android.ocasa.core.activity.BaseActivity;
import com.android.ocasa.loader.MenuTaskLoader;
import com.android.ocasa.model.Application;

import java.util.List;

/**
 * Created by ignacio on 14/01/16.
 */
public class MenuFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Application>>{

    private RecyclerView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (RecyclerView) view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Button qrScanner = (Button) view.findViewById(R.id.qr);
        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)getActivity()).startNewActivity(new Intent(getActivity(), BarcodeActivity.class));
            }
        });
    }

    @Override
    public Loader<List<Application>> onCreateLoader(int i, Bundle bundle) {
        return new MenuTaskLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Application>> loader, List<Application> applications) {

        list.setAdapter(new MenuAdapter(applications));
    }

    @Override
    public void onLoaderReset(Loader<List<Application>> loader) {

    }
}
