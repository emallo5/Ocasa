package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.loader.ReceiptTaskLoader;
import com.android.ocasa.model.Receipt;

/**
 * Created by Emiliano Mallo on 28/03/16.
 */
public class BaseReceiptFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Receipt> {

    static final String ARG_RECEIPT_ID = "receipt_id";

    protected Receipt receipt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public Loader<Receipt> onCreateLoader(int id, Bundle args) {
        return new ReceiptTaskLoader(getActivity(), args.getLong(ARG_RECEIPT_ID));
    }

    @Override
    public void onLoadFinished(Loader<Receipt> loader, Receipt data) {
        receipt = data;
    }

    @Override
    public void onLoaderReset(Loader<Receipt> loader) {

    }
}
