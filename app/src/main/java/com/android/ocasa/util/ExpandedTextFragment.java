package com.android.ocasa.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.ocasa.R;

/**
 * Created by leandro on 5/7/17.
 */

public class ExpandedTextFragment extends FieldDetailDialogFragment {

    private static final String LABEL = "label";
    private static final String VALUE = "value";

    TextView label;
    TextView value;
    Button back;

    public static ExpandedTextFragment newInstance(String label, String value) {

        Bundle args = new Bundle();
        args.putString(LABEL, label);
        args.putString(VALUE, value);

        ExpandedTextFragment fragment = new ExpandedTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,  R.style.SlideDialog);
        setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expanded_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
    }

    private void initControls(View view) {
        label = (TextView) view.findViewById(R.id.tv_label);
        label.setText(getArguments().getString(LABEL));

        value = (TextView) view.findViewById(R.id.tv_value);
        value.setText(getArguments().getString(VALUE));
    }

    @Override
    public String getValue() {
        return "";
    }
}
