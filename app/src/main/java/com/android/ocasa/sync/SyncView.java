package com.android.ocasa.sync;

import com.android.ocasa.session.SessionView;
import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 21/06/16.
 */
public interface SyncView extends SessionView {

    void onSyncFinish();
}
