package com.android.ocasa.widget;

import android.content.Context;
import android.nfc.FormatException;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.util.IconizedMenu;

/**
 * Created by ignacio on 22/02/16.
 */
public class FormTextFieldView extends LinearLayout implements FieldViewAdapter, IconizedMenu.OnMenuItemClickListener{

    private FieldViewActionListener listener;

    private TextView label;
    private TextFieldView field;

    public FormTextFieldView(Context context) {
        this(context, null);
    }

    public FormTextFieldView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormTextFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.form_text_field_layout, this, true);

        label = (TextView) findViewById(R.id.label);
        field = (TextFieldView) findViewById(R.id.field);

        field.getAction().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
    }

    public TextView getLabel(){
        return label;
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
        this.field.getText().setText(value);
        this.field.getText().setTextColor(ContextCompat.getColor(getContext(), R.color.material_blue_grey_800));
    }

    @Override
    public void changeLabelVisbility(boolean visibility) {
        label.setVisibility(VISIBLE);
    }

    @Override
    public String getValue() {
        return field.getText().getText().toString();
    }

    public TextFieldView getField(){
        return field;
    }

    private void showMenu(){
        IconizedMenu actionMenu = new IconizedMenu(getContext(), field.getAction());
        actionMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = actionMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_form_field, actionMenu.getMenu());
        actionMenu.show();
    }

    public FieldViewActionListener getListener() {
        return listener;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                listener.onHistoryClick(this);
                break;
            case R.id.qr_scanner:
                listener.onQrClick(this);
                break;
        }

        return true;
    }
}
