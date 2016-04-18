package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;

import org.w3c.dom.Text;

/**
 * Created by ignacio on 01/02/16.
 */
public class FieldComboView extends LinearLayout implements FieldViewAdapter {

    private FieldViewActionListener listener;

    private TextView label;
    private LinearLayout container;

    private String value;

    public FieldComboView(Context context) {
        super(context, null);
    }

    public FieldComboView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldComboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.field_combo_layout, this, true);

        label = (TextView) findViewById(R.id.label);
        container = (LinearLayout) findViewById(R.id.container);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onComboClick(FieldComboView.this);
            }
        });
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setLabel(String label) {
        this.label.setText(label);
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public LinearLayout getContainer() {
        return container;
    }
}
