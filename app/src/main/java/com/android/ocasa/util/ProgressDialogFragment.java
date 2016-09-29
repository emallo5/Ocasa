package com.android.ocasa.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by ignacio on 25/07/16.
 */
public class ProgressDialogFragment extends DialogFragment {

    static final String ARG_MESSAGE = "message";

    public static ProgressDialogFragment newInstance(String message) {
        
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getArguments().getString(ARG_MESSAGE));
        return dialog;
    }
}
