package com.android.ocasa.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.android.ocasa.R;

/**
 * Created by ignacio on 10/02/16.
 */
public class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    static final String ARG_TITLE = "title";
    static final String ARG_MESSAGE = "message";

    private OnAlertClickListener callback;

    public interface OnAlertClickListener{
        public void onPosiviteClick();
        public void onNegativeClick();
    }

    public static AlertDialogFragment newInstance(String title, String message) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnAlertClickListener) getParentFragment();
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

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.SlideDialog)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", this)
                .setNegativeButton("Cancelar", this)
                .create();

        return alertDialog;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        dismiss();

        if(i == DialogInterface.BUTTON_POSITIVE){
            callback.onPosiviteClick();
        }else if(i == DialogInterface.BUTTON_NEGATIVE){
            callback.onNegativeClick();
        }
    }
}
