package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.ocasa.R;

/**
 * Created by ignacio on 04/02/16.
 */
public class FieldPhoneView extends RelativeLayout implements FieldViewAdapter {

    private FieldViewActionListener listener;

    private FieldTextView fieldTextView;

    public FieldPhoneView(Context context) {
        super(context);

        init();
    }

    public FieldPhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FieldPhoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void setTag(Object tag) {
        super.setTag("");
        fieldTextView.setTag(tag);
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.field_phone_layout, this, true);

        fieldTextView = (FieldTextView) findViewById(R.id.field_text);

        ImageView callButton = (ImageView) findViewById(R.id.call_button);
        callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhoneClick(FieldPhoneView.this);
            }
        });
    }

    public EditText getTextField(){
        return  fieldTextView.getTextField();
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        fieldTextView.setFieldViewActionListener(listener);
        this.listener = listener;
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public String getValue() {
        return fieldTextView.getTextField().getText().toString();
    }
}
