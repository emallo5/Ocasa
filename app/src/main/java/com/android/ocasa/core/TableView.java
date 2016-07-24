package com.android.ocasa.core;

import com.android.ocasa.viewmodel.TableViewModel;
import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 18/07/16.
 */
public interface TableView extends BaseView {
    void onTableLoadSuccess(TableViewModel table);
}
