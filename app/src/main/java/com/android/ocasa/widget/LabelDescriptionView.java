package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;

/**
 * Created by leandro on 5/7/17.
 */

public class LabelDescriptionView extends RelativeLayout {


    public LabelDescriptionView(Context context) {
        super(context);
        init(null);
    }

    public LabelDescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LabelDescriptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public LabelDescriptionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init (AttributeSet attrs) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_label_value, this);

    }

    public void setLabel(String label) {
        ((TextView) findViewById(R.id.tv_label)).setText(label);
    }

    public void setValue(String value) {
        ((TextView) findViewById(R.id.tv_value)).setText(value);
    }
}
