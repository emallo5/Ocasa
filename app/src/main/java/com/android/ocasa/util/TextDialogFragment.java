package com.android.ocasa.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ocasa.R;

/**
 * Created by ignacio on 19/09/16.
 */
public class TextDialogFragment extends FieldDetailDialogFragment{

    private TextView title;
    private EditText text;
    private Button save;

    public static TextDialogFragment newInstance(String fieldName) {

        Bundle args = new Bundle();
        args.putString(ARG_FIELD_NAME, fieldName);

        TextDialogFragment fragment = new TextDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.SlideDialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    private void initControls(View view){
        title = (TextView) view.findViewById(R.id.text_title);
        title.setText("Ingrese " + getArguments().getString(ARG_FIELD_NAME));
        text = (EditText) view.findViewById(R.id.text);
        save = (Button) view.findViewById(R.id.text_save);
    }

    private void setListeners(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValue();
            }
        });
    }

    private void saveValue(){

        if(text.getText().toString().trim().length() == 0){
            Toast.makeText(getActivity(), "Debe ingresar un valor", Toast.LENGTH_SHORT).show();
            return;
        }

        showSaveAlert();
    }


    @Override
    public String getValue() {
        return text.getText().toString();
    }
}
