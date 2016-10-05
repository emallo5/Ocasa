package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 29/09/16.
 */

public class MenuViewModel {

    private List<ApplicationViewModel> applications;

    public MenuViewModel() {
        this.applications = new ArrayList<>();
    }

    public void addApplication(ApplicationViewModel application){
        applications.add(application);
    }

    public List<ApplicationViewModel> getApplications(){
        return applications;
    }
}
