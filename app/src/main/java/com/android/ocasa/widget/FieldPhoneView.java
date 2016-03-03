package com.android.ocasa.widget;

import android.content.Context;
import android.nfc.FormatException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.FieldType;

/**
 * Created by ignacio on 04/02/16.
 */
public class FieldPhoneView extends LinearLayout implements FieldViewAdapter {

    private FieldViewActionListener listener;

    private TextView label;
    private InputFieldView field;

    public FieldPhoneView(Context context) {
        this(context, null);
    }

    public FieldPhoneView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldPhoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /*@Override
    public void setTag(Object tag) {
        super.setTag("");
        field.setTag(tag);
    }*/

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.field_phone_layout, this, true);

        label = (TextView) findViewById(R.id.label);
        field = (InputFieldView) findViewById(R.id.field);

        ImageView callButton = (ImageView) findViewById(R.id.call_button);
        callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhoneClick(FieldPhoneView.this);
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
    public void setValue(String value) throws FormatException{

        if(FieldType.PHONE.checkValue(value))
            throw new FormatException();

        field.getInput().setText(value);
    }

    @Override
    public String getValue() {
        return field.getInput().getText().toString();
    }

    public InputFieldView getField() {
        return field;
    }
}
