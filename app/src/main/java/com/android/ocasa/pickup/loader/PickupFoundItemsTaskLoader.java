package com.android.ocasa.pickup.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 25/04/16.
 */
public class PickupFoundItemsTaskLoader extends AsyncTaskLoader<TableViewModel> {

    protected List<String> codes;
    protected String tableId;

    private TableViewModel table;

    public PickupFoundItemsTaskLoader(Context context, String tableId, List<String> codes) {
        super(context);
        this.tableId = tableId;
        this.codes = codes;
    }

    @Override
    public TableViewModel loadInBackground() {

        TableViewModel tableViewModel = new TableViewModel();

        List<Record> records = getOcurrences();

        for (int index = 0; index < records.size(); index++){

            Record record = records.get(index);

            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());
            cell.setValue(record.getPrimaryKey().getValue());

            fillCell(cell, record.getLogicFields());
            tableViewModel.addCell(cell);
        }

        return tableViewModel;
    }

    private List<Record> getOcurrences(){
        List<Record> ocurrences = new ArrayList<>();

        for (int index = 0; index < codes.size(); index++){
            Record record = RecordDAO.getInstance(getContext()).findForTableAndValueId(tableId, codes.get(index));
            if(record != null)
                ocurrences.add(record);
        }

        return ocurrences;
    }

    public void fillCell(CellViewModel cell, List<Field> fields){

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        for (Field field : fields){
            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());
            fieldViewModel.setTag(field.getColumn().getId());
            fieldViewModel.setLabel(field.getColumn().getName());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }

    @Override
    public void deliverResult(TableViewModel data) {
        this.table = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(table != null)
            deliverResult(table);
        else{
            forceLoad();
        }
    }
}
