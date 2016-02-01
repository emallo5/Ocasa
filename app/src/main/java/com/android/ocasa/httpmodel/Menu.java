package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Application;

import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class Menu {

    private List<Application> applications;

    public Menu(){ }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
