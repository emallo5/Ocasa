package com.android.ocasa.core;

import com.android.ocasa.viewmodel.FormViewModel;
import com.codika.androidmvp.view.BaseView;

/**
 * Created by ignacio on 11/07/16.
 */
public interface FormView extends BaseView {
    void onFormSuccess(FormViewModel form);
}
