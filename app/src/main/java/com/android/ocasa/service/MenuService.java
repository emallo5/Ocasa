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

                for (Table table : category.getTables()){
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
                    }

                    ActionDAO actionDAO = new ActionDAO(context);
                    actionDAO.save(category.getActions());
                }

                TableDAO dao = new TableDAO(context);
                dao.save(category.getTables());
            }

            CategoryDAO dao = new CategoryDAO(context);
            dao.save(application.getCategories());
        }

        ApplicationDAO dao = new ApplicationDAO(context);
        dao.save(menu.getApplications());
    }
}
