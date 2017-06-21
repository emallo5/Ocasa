package com.android.ocasa.widget;

import android.content.Context;
import android.nfc.FormatException;
import android.util.AttributeSet;
import android.view.View;

import com.android.ocasa.model.FieldType;

/**
 * Created by ignacio on 04/02/16.
 */
public class FieldTimeView extends FormTextFieldView {

    public FieldTimeView(Context context) {
        this(context, null);
    }

    public FieldTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getField().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getListener().onTimeClick(FieldTimeView.this);
            }
        });
    }

    @Override
    public void setValue(String value) throws FormatException{

        if(!FieldType.TIME.isValidValue(value))
            throw new FormatException();

        super.setValue(value);
    }

    @Override
    public void changeLabelVisbility(boolean visibility) {
//        label.setVisibility(VISIBLE);
    }
}

