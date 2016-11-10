package com.android.ocasa.settings;

import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 10/11/16.
 */

public interface SettingsView extends BaseView{
    void onSyncSuccess();
    void onLogoutSuccess();
}
