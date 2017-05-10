package com.android.ocasa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ocasa.R;

/**
 * Created by leandro on 8/5/17.
 */

public class TagReaderView extends RelativeLayout {

    private TextView title;
    private EditText code;
    private ImageView readQr;
    private OnActionClickListener callback;

    public TagReaderView(Context context) {
        this(context, null);
    }

    public TagReaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.field_tag_reader_layout, this, true);

        title = (TextView) findViewById(R.id.tv_title);
        code = (EditText) findViewById(R.id.et_input);
        readQr = (ImageView) findViewById(R.id.read_qr);

        readQr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onReadTagClick();
            }
        });

        code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if (callback != null) callback.onEnterKeyPressed(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void setCodeRead(String code) {
        this.code.setText(code);
    }

    public void setCountRead(int count) {
        this.title.setText(getContext().getString(R.string.read_tags_title).replace("[X]", String.valueOf(count)));
    }

    public void setOnActionClickListener(OnActionClickListener action) {
        this.callback = action;
    }

    public interface OnActionClickListener {
        void onReadTagClick();
        void onEnterKeyPressed(String code);
    }
}
