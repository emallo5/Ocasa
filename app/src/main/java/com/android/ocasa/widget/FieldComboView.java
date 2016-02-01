package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ignacio on 01/02/16.
 */
public class FieldComboView extends TextView implements FieldViewAdapter {

    private FieldViewActionListener listener;

    public FieldComboView(Context context) {
        super(context);

        init();
    }

    public FieldComboView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FieldComboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onComboClick();
            }
        });
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }
}
