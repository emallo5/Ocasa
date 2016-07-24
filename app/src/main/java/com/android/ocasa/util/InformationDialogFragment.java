package com.android.ocasa.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.android.ocasa.R;

/**
 * Created by ignacio on 16/06/16.
 */
public class InformationDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    static final String ARG_TITLE = "title";
    static final String ARG_MESSAGE = "message";

    private OnIformationClickListener callback;

    public interface OnIformationClickListener{
        public void onCloseDialog();
    }

    public static InformationDialogFragment newInstance(String title, String message) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);

        InformationDialogFragment fragment = new InformationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(getParentFragment() != null)
                callback = (OnIformationClickListener) getParentFragment();
            else
                callback = (OnIformationClickListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    getParentFragment().getClass().getName() + " must implements OnAlertClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(ARG_TITLE);
        String message = getArguments().getString(ARG_MESSAGE);

        return new AlertDialog.Builder(getActivity(), R.style.SlideDialog)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        dismiss();

        callback.onCloseDialog();
    }
}
