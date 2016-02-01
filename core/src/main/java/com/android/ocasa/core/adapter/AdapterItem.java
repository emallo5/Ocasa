package com.android.ocasa.core.adapter;

/**
 * Created by ignacio on 18/01/16.
 */
public class AdapterItem {

    private int type;
    private Object data;

    public AdapterItem(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
