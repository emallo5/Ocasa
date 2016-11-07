package com.android.ocasa.widget;

import android.content.Context;
import android.nfc.FormatException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.ocasa.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by ignacio on 20/10/16.
 */

public class FieldPhotoView extends RelativeLayout implements FieldViewAdapter{

    private FieldViewActionListener listener;

    private TextView label;
    private ImageView photo;
    private ImageView edit;

    public FieldPhotoView(Context context) {
        this(context, null);
    }

    public FieldPhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.field_photo_layout, this, true);

        label = (TextView) findViewById(R.id.label);
        photo = (ImageView) findViewById(R.id.photo);
        edit = (ImageView) findViewById(R.id.edit);
        edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditPhotoClick(FieldPhotoView.this);
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
    public void setValue(String value) throws FormatException {

        if(value == null || value.isEmpty()){
            return;
        }

        File file = new File(getContext().getCacheDir(), value);

        ImageLoader.getInstance().displayImage("file://" + file.getPath(), photo);
    }

    @Override
    public void changeLabelVisbility(boolean visibility) {
        label.setVisibility(VISIBLE);
    }

    @Override
    public String getValue() {
        return null;
    }
}
