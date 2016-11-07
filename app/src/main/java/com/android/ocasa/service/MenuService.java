package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.cache.dao.ActionDAO;
import com.android.ocasa.cache.dao.ApplicationDAO;
import com.android.ocasa.cache.dao.CategoryDAO;
import com.android.ocasa.cache.dao.ColumnActionDAO;
import com.android.ocasa.cache.dao.LayoutDAO;
import com.android.ocasa.cache.dao.TableDAO;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.ApplicationViewModel;
import com.android.ocasa.viewmodel.CategoryViewModel;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.android.ocasa.viewmodel.OptionViewModel;

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

                LayoutDAO layoutDAO = new LayoutDAO(context);
                TableDAO dao = new TableDAO(context);

                List<Table> tables = new ArrayList<>();

                for (Layout layout : category.getLayouts()){
                    Table table = layout.getTable();
                    tables.add(table);
                }

                if(category.getActions() != null) {
                    ColumnActionDAO columnDAO = new ColumnActionDAO(context);

                    for (Action action : category.getActions()) {
                        action.setCategory(category);

                        columnDAO.deleteForActionId(action.getId());

                        for (ColumnAction header: action.getColumnsHeader()) {
                            header.setAction(action);
                        }

                        if(action.getTable().getId().equalsIgnoreCase("OM_MovilNovedad")){

                            Column sigature = new Column();
                            sigature.setId("OM_MovilNovedad_cf_0400");
                            sigature.setName("Firma");
                            sigature.setVisible(true);
                            sigature.setLogic(true);
                            sigature.setFieldType(FieldType.SIGNATURE);

                            ColumnAction columnAction = new ColumnAction();
                            columnAction.setType(ColumnAction.ColumnActionType.DETAIL);
                            columnAction.setColumn(sigature);
                            columnAction.setEditable(true);
                            columnAction.setLastValue("");
                            columnAction.setDefaultValue("");

                            action.getColumnsDetail().add(columnAction);

                            Column photo = new Column();
                            photo.setId("OM_MovilNovedad_cf_0500");
                            photo.setName("Foto");
                            photo.setVisible(true);
                            photo.setLogic(true);
                            photo.setFieldType(FieldType.PHOTO);

                            ColumnAction photoAction = new ColumnAction();
                            photoAction.setType(ColumnAction.ColumnActionType.DETAIL);
                            photoAction.setColumn(photo);
                            photoAction.setEditable(true);
                            photoAction.setLastValue("");
                            photoAction.setDefaultValue("");

                            action.getColumnsDetail().add(photoAction);
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

                layoutDAO.save(category.getLayouts());
                dao.save(tables);
            }

            CategoryDAO dao = new CategoryDAO(context);
            dao.save(application.getCategories());
        }

        ApplicationDAO dao = new ApplicationDAO(context);
        dao.save(menu.getApplications());
    }

    public MenuViewModel getMenu(Context context){

        MenuViewModel menu = new MenuViewModel();

        List<Application> apps = new ApplicationDAO(context).findAll();

        for (int index = 0; index < apps.size(); index++) {

            Application app = apps.get(index);

            ApplicationViewModel applicationViewModel = new ApplicationViewModel();
            applicationViewModel.setTitle(app.getName());

            List<Category> categories = new ArrayList<>(app.getCategories());

            for (int subIndex = 0; subIndex < categories.size(); subIndex++) {
                Category category = categories.get(subIndex);

                if (category.isVisible()) {

                    CategoryViewModel categoryViewModel = new CategoryViewModel();
                    categoryViewModel.setTitle(category.getName());
                    applicationViewModel.addCategory(categoryViewModel);

                    List<Table> visibleTables = new ArrayList<>();

                    if (category.getActions() == null || category.getActions().size() == 0) {
                        for (Layout layout : category.getLayouts()) {
                            categoryViewModel.addOption(newOption(layout.getExternalID(),
                                    layout.getTable().getName(),
                                    OptionViewModel.TABLE));
                        }
                    }

                    for (Action action : category.getActions()) {
                        categoryViewModel.addOption(newOption(action.getId(),
                                action.getName(),
                                OptionViewModel.ACTION));
                    }

                    category.setTables(visibleTables);
                }
            }

            menu.addApplication(applicationViewModel);
        }

        return menu;
    }

    private OptionViewModel newOption(String id, String title, int type){
        OptionViewModel newOption = new OptionViewModel();
        newOption.setId(id);
        newOption.setTitle(title);
        newOption.setType(type);

        return newOption;
    }
}
