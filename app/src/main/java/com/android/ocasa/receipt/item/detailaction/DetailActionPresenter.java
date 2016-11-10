package com.android.ocasa.receipt.item.detailaction;

import com.android.ocasa.core.FormPresenter;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.service.OcasaService;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.codika.androidmvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 24/10/16.
 */

public class DetailActionPresenter extends FormPresenter {

    public void loadFields(long receiptId){
        FormViewModel form = OcasaService.getInstance().getDetailFormForReceipt(receiptId);

        getView().onFormSuccess(form);
    }
}
