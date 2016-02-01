package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ignacio on 01/02/16.
 */
public class FieldDateView extends TextView implements FieldViewAdapter{

    private FieldViewActionListener listener;

    public FieldDateView(Context context, final LatLng location) {
        super(context);

        init();
    }

    public FieldDateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FieldDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateClick();
            }
        });
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

}
