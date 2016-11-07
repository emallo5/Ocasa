package com.android.ocasa.receipt.item.detailaction;

import com.android.ocasa.viewmodel.FieldViewModel;
import com.codika.androidmvp.view.BaseView;

import java.util.List;

/**
 * Created by ignacio on 24/10/16.
 */

public interface DetailActionView extends BaseView {
    void onFieldsSuccess(List<FieldViewModel> fields);
}
