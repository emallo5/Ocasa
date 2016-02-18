package com.android.ocasa.core.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.android.ocasa.core.R;

/**
 * Created by ignacio on 01/02/16.
 */
public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(STYLE_NO_FRAME, R.style.SlideDialog);
    }
}
