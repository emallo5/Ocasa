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
        List<Column> detail = OcasaService.getInstance().getDetailColumnsForReceipt(receiptId);

        FormViewModel form = new FormViewModel();
        form.setColor("#33BDC2");

        for (Column column: detail) {
            if (column.getFieldType() != FieldType.COMBO) {
                FieldViewModel field = new FieldViewModel();
                field.setTag(column.getId());
                field.setLabel(column.getName());
                field.setType(column.getFieldType());
                field.setEditable(true);

                form.addField(field);
            }
        }

        getView().onFormSuccess(form);
    }
}
