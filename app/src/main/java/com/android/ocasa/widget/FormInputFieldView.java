package com.android.ocasa.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
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
public class FormInputFieldView extends LinearLayout implements FieldViewAdapter, IconizedMenu.OnMenuItemClickListener{

    private FieldViewActionListener listener;

    //private TextView label;
    private InputFieldView field;

    public FormInputFieldView(Context context) {
        this(context, null);
    }

    public FormInputFieldView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormInputFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.form_input_field_layout, this, true);

        //label = (TextView) findViewById(R.id.label);
        field = (InputFieldView) findViewById(R.id.field);

        field.getAction().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
    }

    @Override
    public void setFieldViewActionListener(FieldViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setLabel(String label) {
        field.setHint(label);
    }

    @Override
    public void setValue(String value) {
        this.field.getInput().setText(value);
    }

    @Override
    public void changeLabelVisbility(boolean visibility) {
//        field.getInputLayout().setHintEnabled(visibility);
        field.getInputLayout().setVisibility(visibility ? VISIBLE : GONE);
    }

    @Override
    public String getValue() {
        return field.getInput().getText().toString();
    }

    public InputFieldView getField(){
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
