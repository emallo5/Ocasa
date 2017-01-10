package com.android.ocasa.settings;

import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 10/11/16.
 */

public interface SettingsView extends BaseView {

    void notConnection();
    void onSyncSuccess();
    void onLogoutSuccess();
    void canLogout();
    void needUpload();
    void onUploadSuccess();
}
