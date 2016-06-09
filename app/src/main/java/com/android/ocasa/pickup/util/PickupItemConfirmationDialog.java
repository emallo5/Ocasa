package com.android.ocasa.pickup.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.android.ocasa.R;

/**
 * Created by Emiliano Mallo on 28/04/16.
 */
public class PickupItemConfirmationDialog extends DialogFragment {

    static final String ARG_CODE = "code";

    private OnConfirmationListener callback;

    public interface OnConfirmationListener{
        public void onCancel();
        public void onAdd(String code);
    }

    public static PickupItemConfirmationDialog newInstance(String code) {

        Bundle args = new Bundle();
        args.putString(ARG_CODE, code);

        PickupItemConfirmationDialog fragment = new PickupItemConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnConfirmationListener) activity;
        }catch (ClassCastException e){

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

        dialog.setTitle(R.string.confirmation_dialog_title);
        dialog.setMessage(getString(R.string.confirmation_dialog_message, getArguments().getString(ARG_CODE)));
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                callback.onCancel();
//            }
//        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onAdd(getArguments().getString(ARG_CODE));
            }
        });

        return dialog;
    }
}
