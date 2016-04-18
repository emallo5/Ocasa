package com.android.ocasa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.android.ocasa.core.fragment.BaseFragment;
import com.android.ocasa.loader.ActionTaskLoader;
import com.android.ocasa.model.Action;

/**
 * Iganecio Oviedo on 21/03/16.
 */
public class BaseActionFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Action> {

    static final String ARG_ACTION_ID = "action_id";

    protected Action action;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public Loader<Action> onCreateLoader(int id, Bundle args) {
        return new ActionTaskLoader(getActivity(), args.getString(ARG_ACTION_ID));
    }

    @Override
    public void onLoadFinished(Loader<Action> loader, Action data) {
        this.action = data;
    }

    @Override
    public void onLoaderReset(Loader<Action> loader) {

    }

    public Action getAction() {
        return action;
    }
}
