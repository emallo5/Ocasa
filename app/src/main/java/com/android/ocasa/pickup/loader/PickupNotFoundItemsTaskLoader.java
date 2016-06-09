package com.android.ocasa.pickup.loader;

import android.content.Context;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 25/04/16.
 */
public class PickupNotFoundItemsTaskLoader extends PickupFoundItemsTaskLoader {

    public PickupNotFoundItemsTaskLoader(Context context, String tableId, List<String> codes) {
        super(context, tableId, codes);
    }

    @Override
    public TableViewModel loadInBackground() {

        TableViewModel tableViewModel = new TableViewModel();

        List<String> codes = getNotOcurrences();

        for (int index = 0; index < codes.size(); index++){

            CellViewModel cell = new CellViewModel();

            FieldViewModel field = new FieldViewModel();
            field.setValue(codes.get(index));

            List<FieldViewModel> fields = new ArrayList<>();
            fields.add(field);

            cell.setFields(fields);

            tableViewModel.addCell(cell);
        }

        return tableViewModel;
    }

    private List<String> getNotOcurrences(){
        List<String> notOcurrences = new ArrayList<>();

        for (int index = 0; index < codes.size(); index++){
            Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(tableId, codes.get(index));
            if(record == null)
                notOcurrences.add(codes.get(index));
        }

        return notOcurrences;
    }
}
