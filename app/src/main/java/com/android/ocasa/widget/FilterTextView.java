package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;

/**
 * Created by ignacio on 15/02/16.
 */
public class FilterTextView extends LinearLayout {

    private TextView label;

    public FilterTextView(Context context) {
        this(context, null);
    }

    public FilterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.filter_text_layout, this, true);

        label = (TextView) findViewById(R.id.label);

    }

    public void setLabel(String label){
        this.label.setText(label);
    }
}
