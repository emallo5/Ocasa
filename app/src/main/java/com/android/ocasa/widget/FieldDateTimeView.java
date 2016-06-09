package com.android.ocasa.widget;

import android.content.Context;
import android.nfc.FormatException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.util.DateTimeHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Emiliano Mallo on 09/05/16.
 */
public class FieldDateTimeView extends LinearLayout implements FieldViewAdapter{

    private TextView label;

    private FieldDateView dateView;
    private FieldTimeView timeView;

    private ImageView action;

    private FieldViewActionListener listener;

    public FieldDateTimeView(Context context) {
        this(context, null);
    }

    public FieldDateTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldDateTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.field_datetime_layout, this, true);

        label = (TextView) findViewById(R.id.label);

        dateView = (FieldDateView) findViewById(R.id.date);
        dateView.getLabel().setVisibility(GONE);
        dateView.getField().getAction().setVisibility(GONE);

        timeView = (FieldTimeView) findViewById(R.id.time);
        timeView.getLabel().setVisibility(GONE);
        timeView.getField().getAction().setVisibility(GONE);

        action = (ImageView) findViewById(R.id.action);

        try {
            Date date = new Date();
            dateView.setValue(DateTimeHelper.formatDate(date));
            timeView.setValue(DateTimeHelper.formatTime(date));
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
        dateView.setFieldViewActionListener(listener);
        timeView.setFieldViewActionListener(listener);
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        dateView.setTag(tag);
        timeView.setTag(tag);
    }

    @Override
    public void setLabel(String label) {
        this.label.setText(label);
    }

    public ImageView getAction(){
        return action;
    }

    @Override
    public void setValue(String value) throws FormatException {

        Date dateTime = DateTimeHelper.parseDateTime(value);

        if(dateTime == null)
            throw new FormatException();

        setDate(DateTimeHelper.formatDate(dateTime));
        setTime(DateTimeHelper.formatTime(dateTime));
    }

    public void setDate(String value) throws FormatException {
        dateView.setValue(value);
    }

    public void setTime(String value) throws FormatException {
        timeView.setValue(value);
    }


    @Override
    public void changeLabelVisbility(boolean visibility) {
        label.setVisibility(VISIBLE);
    }

    @Override
    public String getValue() {
        Calendar date = Calendar.getInstance();
        date.setTime(DateTimeHelper.parseDate(dateView.getValue()));

        Calendar time = Calendar.getInstance();
        time.setTime(DateTimeHelper.parseTime(timeView.getValue()));

        Calendar validityDate = Calendar.getInstance();
        validityDate.set(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE),
                0);

        return DateTimeHelper.formatDateTime(validityDate.getTime());
    }

    public TextView getLabel() {
        return label;
    }
}
