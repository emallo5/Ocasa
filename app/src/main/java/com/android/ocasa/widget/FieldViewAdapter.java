package com.android.ocasa.widget;

import android.nfc.FormatException;

/**
 * Created by ignacio on 28/01/16.
 */
public interface FieldViewAdapter {

    public void setFieldViewActionListener(FieldViewActionListener listener);
    public void setLabel(String label);
    public void setValue(String value) throws FormatException;
    public String getValue();

}
