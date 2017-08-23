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
    static final String ARG_POSITIVE = "positive";
    static final String ARG_NEGATIVE = "negative";
    static final String ARG_NEUTRAL = "neutral";

    private OnAlertClickListener callback;

    public interface OnAlertClickListener{
        void onPosiviteClick(String tag);
        void onNeutralClick(String tag);
        void onNegativeClick(String tag);
    }

    public static AlertDialogFragment newInstance(String title, String message) {
        return newInstance(title, message, null, null, null);
    }

    public static AlertDialogFragment newInstance(String title, String message,
                                                  String positive,
                                                  String negative,
                                                  String neutral) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE, positive);
        args.putString(ARG_NEGATIVE, negative);
        args.putString(ARG_NEUTRAL, neutral);

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(getParentFragment() != null)
                callback = (OnAlertClickListener) getParentFragment();
            else
                callback = (OnAlertClickListener) getActivity();
        }catch (ClassCastException e){
//            throw new ClassCastException(
//                    getParentFragment().getClass().getName() + " must implements OnAlertClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(ARG_TITLE);
        String message = getArguments().getString(ARG_MESSAGE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SlideDialog)
                .setTitle(title)
                .setMessage(message);

        if (getArguments().getString(ARG_NEGATIVE) != null) {
            builder.setNegativeButton(getArguments().getString(ARG_NEGATIVE), this);
        } else {
            builder.setNegativeButton(android.R.string.cancel, this);
        }

        if (getArguments().getString(ARG_POSITIVE) != null) {
            builder.setPositiveButton(getArguments().getString(ARG_POSITIVE), this);
        } else {
            builder.setPositiveButton(android.R.string.ok, this);
        }

        if (getArguments().getString(ARG_NEUTRAL) != null) {
            builder.setNeutralButton(getArguments().getString(ARG_NEUTRAL), this);
        }

        return builder.create();
    }

    public void setCallback(OnAlertClickListener listener) {
        this.callback = listener;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        dismiss();

        if (callback == null) return;

        if(i == DialogInterface.BUTTON_POSITIVE) {
            callback.onPosiviteClick(getTag());
        }else if(i == DialogInterface.BUTTON_NEGATIVE){
            callback.onNegativeClick(getTag());
        }else if(i == DialogInterface.BUTTON_NEUTRAL){
            callback.onNeutralClick(getTag());
        }
    }
}
