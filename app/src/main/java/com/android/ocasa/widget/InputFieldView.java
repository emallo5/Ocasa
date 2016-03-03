package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.ocasa.R;

/**
 * Created by ignacio on 22/02/16.
 */
public class InputFieldView extends RelativeLayout{

    private EditText input;
    private ImageView action;

    public InputFieldView(Context context) {
        this(context, null);
    }

    public InputFieldView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.field_input_layout, this, true);

        input = (EditText) findViewById(R.id.input);
        action = (ImageView) findViewById(R.id.action);
    }

    public EditText getInput() {
        return input;
    }

    public ImageView getAction() {
        return action;
    }
}
