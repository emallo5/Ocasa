package com.android.ocasa.viewmodel;

/**
 * Created by ignacio on 29/09/16.
 */

public class OptionViewModel {

    public static final int ACTION = 0;
    public static final int TABLE = 1;

    private String id;

    private String title;

    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAction(){
        return type == ACTION;
    }

    public boolean isTable(){
        return type == TABLE;
    }
}
