package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;

/**
 * Created by ignacio on 22/02/16.
 */
public class TextFieldView extends RelativeLayout {

    private TextView text;
    private ImageView action;

    public TextFieldView(Context context) {
        this(context, null);
    }

    public TextFieldView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.field_text_layout, this, true);

        text = (TextView) findViewById(R.id.text);
        action = (ImageView) findViewById(R.id.action);
    }

    public TextView getText() {
        return text;
    }

    public ImageView getAction() {
        return action;
    }
}
