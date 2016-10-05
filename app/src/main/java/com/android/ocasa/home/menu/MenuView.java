package com.android.ocasa.home.menu;

import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.model.Application;
import com.android.ocasa.viewmodel.MenuViewModel;
import com.codika.androidmvp.view.BaseView;

import java.util.List;

/**
 * Created by ignacio on 18/07/16.
 */
public interface MenuView extends BaseView {
    void onMenuLoadSuccess(MenuViewModel menu);
}
