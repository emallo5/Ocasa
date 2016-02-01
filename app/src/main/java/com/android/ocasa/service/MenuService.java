package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.BuildConfig;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.http.listener.RequestCallback;
import com.android.ocasa.http.service.HttpService;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.Table;
import com.android.ocasa.service.notification.NotificationManager;
import com.android.volley.VolleyError;

/**
 * Created by ignacio on 18/01/16.
 */
public class MenuService {

    static final String TAG = "MenuService";

    static final String MENU_URL = "/android/";

    public static final String MENU_SYNC_FINISHED_ACTION = "com.android.ocasa.service.MenuService.MENU_SYNC_FINISHED_ACTION";
    public static final String MENU_SYNC_ERROR_ACTION = "com.android.ocasa.service.MenuService.MENU_SYNC_ERROR_ACTION";

    private Context context;

    public MenuService(Context context){
        this.context = context;
    }

    public void requestMenu(RequestCallback<Menu> callback){

        HttpService service = HttpService.getInstance(context);

        service.newGetRequest(BuildConfig.BASE_URL + MENU_URL, Menu.class, callback);
    }

    public static class SaveMenuResponseCallback extends GenericRequestCallback<Menu>{

        public SaveMenuResponseCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Menu response) {
            super.onSuccess(response);
            saveMenu(response);

            NotificationManager.sendBroadcast(getContext(), MENU_SYNC_FINISHED_ACTION);
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);

            NotificationManager.sendBroadcast(getContext(), MENU_SYNC_ERROR_ACTION);
        }

        private void saveMenu(Menu menu){

            for (Application application : menu.getApplications()){

                for (Category category : application.getCategories()){
                    category.setApplication(application);

                    for (Table table : category.getTables()){
                        table.setCategory(category);
                    }

                    TableDAO dao = new TableDAO(getContext());
                    dao.save(category.getTables());
                }

                CategoryDAO dao = new CategoryDAO(getContext());
                dao.save(application.getCategories());
            }

            ApplicationDAO dao = new ApplicationDAO(getContext());
            dao.save(menu.getApplications());
        }
    }

}
