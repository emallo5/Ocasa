package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ignacio on 25/02/16.
 */
public class FieldListView extends TextView implements FieldViewAdapter{

    private String value;

    private FieldViewActionListener listener;

    public FieldListView(Context context) {
        this(context, null);
    }

    public FieldListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListClick(FieldListView.this);
            }
        });

    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setLabel(String label) {
        setText(label);
    }

    public String getLabel(){
        return getText().toString();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void changeLabelVisbility(boolean visibility) {

    }

    @Override
    public String getValue() {
        return value;
    }
}
