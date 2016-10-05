package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 29/09/16.
 */

public class CategoryViewModel {

    private String title;

    private List<OptionViewModel> options;

    public CategoryViewModel(){
        options = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addOption(OptionViewModel option){
        options.add(option);
    }

    public List<OptionViewModel> getOptions(){
        return options;
    }
}
