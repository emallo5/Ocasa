package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.util.IconizedMenu;

/**
 * Created by ignacio on 01/02/16.
 */
public class FieldDateView extends RelativeLayout implements FieldViewAdapter, IconizedMenu.OnMenuItemClickListener{

    private FieldViewActionListener listener;

    private TextView textView;

    private ImageView qrScanner;

    public FieldDateView(Context context){
        super(context);
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

        LayoutInflater.from(getContext()).inflate(R.layout.field_date_layout, this, true);

        textView = (TextView) findViewById(R.id.text_field);
        qrScanner = (ImageView) findViewById(R.id.qr_scanner);

        qrScanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateClick(FieldDateView.this);
            }
        });
    }

    private void showMenu(){
        IconizedMenu actionMenu = new IconizedMenu(getContext(), qrScanner);
        actionMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = actionMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_form_field, actionMenu.getMenu());
        actionMenu.show();
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setValue(String value) {
        textView.setText(value);
    }

    @Override
    public String getValue() {
        return textView.getText().toString();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                listener.onHistoryClick(Integer.valueOf(getTag().toString()));
                break;
            case R.id.qr_scanner:
                listener.onQrClick(Integer.valueOf(getTag().toString()));
                break;
        }

        return true;
    }
}
