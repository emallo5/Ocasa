package com.android.ocasa.widget;

/**
 * Created by ignacio on 28/01/16.
 */
public interface FieldViewAdapter {

    public void setFieldViewActionListener(FieldViewActionListener listener);
    public void setValue(String value);
    public String getValue();

}
