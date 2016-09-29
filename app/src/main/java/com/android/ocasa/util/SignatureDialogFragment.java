package com.android.ocasa.util;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.ocasa.R;
import com.android.ocasa.widget.SignatureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ignacio on 12/09/16.
 */
public class SignatureDialogFragment extends FieldDetailDialogFragment {

    static final String SIGNATURE_FILE_NAME = "signature_%s.jpg";

    private TextView title;
    private SignatureView signature;
    private Button clean;
    private Button save;


    public static SignatureDialogFragment newInstance(String fieldName, long recordId) {

        Bundle args = new Bundle();
        args.putString(ARG_FIELD_NAME, fieldName);

        SignatureDialogFragment fragment = new SignatureDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,  R.style.SlideDialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signature_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControls(view);
        setListeners();
    }

    private void initControls(View view){
        title = (TextView) view.findViewById(R.id.signature_title);
        title.setText("Ingrese " + getArguments().getString(ARG_FIELD_NAME) );
        signature = (SignatureView) view.findViewById(R.id.signature);
        clean = (Button) view.findViewById(R.id.signature_clean);
        save = (Button) view.findViewById(R.id.signature_save);
    }

    private void setListeners(){
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signature.clear();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signature.hasSignature()) {
                    doSave();
                }
            }
        });
    }

    private void doSave(){
        String path = saveSignature();

        if(path == null){
            getOnFieldSaveListener().onError();
        }else{
            getOnFieldSaveListener().onSave(path);
        }

        dismiss();
    }

    private String saveSignature(){

        Bitmap signature = this.signature.getSignature();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String fileName = String.format(SIGNATURE_FILE_NAME, timeStamp);

        File file = new File(getActivity().getCacheDir(), fileName);

        try {

            if(!file.exists()) {
                if (file.createNewFile()) {
                    OutputStream stream = new FileOutputStream(file);
                    signature.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    stream.flush();
                    stream.close();

                    return fileName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String getValue() {
        return saveSignature();
    }
}
