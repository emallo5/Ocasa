package com.android.ocasa.widget;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.ocasa.R;
import com.android.ocasa.util.IconizedMenu;

/**
 * Created by ignacio on 04/02/16.
 */
public class FieldTextView extends RelativeLayout implements FieldViewAdapter, IconizedMenu.OnMenuItemClickListener{

    private FieldViewActionListener listener;

    private EditText textField;
    private ImageView qrScanner;

    public FieldTextView(Context context) {
        super(context);

        init();
    }

    public FieldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FieldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.field_text_layout, this, true);

        textField = (EditText) findViewById(R.id.text_field);
        qrScanner = (ImageView) findViewById(R.id.qr_scanner);

        qrScanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
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

    public EditText getTextField() {
        return textField;
    }

    public ImageView getQrButton() {
        return qrScanner;
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setValue(String value) {
        textField.setText(value);
    }

    @Override
    public String getValue() {
        return textField.getText().toString();
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
