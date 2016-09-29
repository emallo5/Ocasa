package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class MenuService {

    public MenuService(){
    }

    public void save(Context context, Menu menu){
        for (Application application : menu.getApplications()){

            for (Category category : application.getCategories()){
                category.setApplication(application);

                TableDAO dao = new TableDAO(context);

                for (Table table : category.getTables()){
                    table.setVisible(category.getActions().size() == 0);
                    table.setCategory(category);
                }

                if(category.getActions() != null) {
                    ColumnActionDAO columnDAO = new ColumnActionDAO(context);

                    for (Action action : category.getActions()) {
                        action.setCategory(category);

                        columnDAO.deleteForActionId(action.getId());

                        for (ColumnAction header: action.getColumnsHeader()) {
                            header.setAction(action);
                        }

                        for (ColumnAction detail: action.getColumnsDetail()) {
                            detail.setAction(action);
                        }

                        columnDAO.save(action.getColumnsHeader());
                        columnDAO.save(action.getColumnsDetail());

                        Table table = dao.findById(action.getTable().getId());

                        if(table == null){
                            dao.save(action.getTable());
                        }
                    }

                    ActionDAO actionDAO = new ActionDAO(context);
                    actionDAO.save(category.getActions());
                }

                dao.save(category.getTables());
            }

            CategoryDAO dao = new CategoryDAO(context);
            dao.save(application.getCategories());
        }

        ApplicationDAO dao = new ApplicationDAO(context);
        dao.save(menu.getApplications());
    }

    public List<Application> getMenu(Context context){

        List<Application> apps = new ApplicationDAO(context).findAll();

        for (int index = 0; index < apps.size(); index++) {

            Application app = apps.get(index);

            List<Category> visibleCategories = new ArrayList<>();

            List<Category> categories = new ArrayList<>(app.getCategories());

            for (int subIndex = 0; subIndex < categories.size(); subIndex++) {
                Category category = categories.get(subIndex);

                if (category.isVisible()) {
                    visibleCategories.add(category);

                    List<Table> visibleTables = new ArrayList<>();

                    for (Table table: category.getTables()) {
                        if(table.isVisible()){
                            visibleTables.add(table);
                        }
                    }

                    category.setTables(visibleTables);
                }
            }

            app.setCategories((ArrayList<Category>) visibleCategories);
        }

        return apps;
    }
}
