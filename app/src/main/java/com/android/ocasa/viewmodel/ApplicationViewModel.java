package com.android.ocasa.viewmodel;

import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 29/09/16.
 */

public class ApplicationViewModel {

    private String title;

    private List<CategoryViewModel> categories;

    public ApplicationViewModel(){
        categories = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addCategory(CategoryViewModel category){
        categories.add(category);
    }

    public List<CategoryViewModel> getCategories(){
        return categories;
    }
}
