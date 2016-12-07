package com.android.ocasa.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.util.IconizedMenu;

/**
 * Created by ignacio on 01/02/16.
 */
public class FieldComboView extends RelativeLayout implements FieldViewAdapter, IconizedMenu.OnMenuItemClickListener {

    private FieldViewActionListener listener;

    private TextView label;
    private LinearLayout container;
    private ImageView action;

    private String value;

    public FieldComboView(Context context) {
        super(context, null);
    }

    public FieldComboView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FieldComboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.field_combo_layout, this, true);

        label = (TextView) findViewById(R.id.label);
        container = (LinearLayout) findViewById(R.id.container);
        action = (ImageView) findViewById(R.id.action);
        action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onComboClick(FieldComboView.this);
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
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void changeLabelVisbility(boolean visibility) {
        label.setVisibility(VISIBLE);
    }

    public ImageView getAction(){
        return action;
    }

    @Override
    public String getValue() {
        return value;
    }

    public LinearLayout getContainer() {
        return container;
    }

    private void showMenu(){
        IconizedMenu actionMenu = new IconizedMenu(getContext(), action);
        actionMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = actionMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_form_field, actionMenu.getMenu());
        actionMenu.show();
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
