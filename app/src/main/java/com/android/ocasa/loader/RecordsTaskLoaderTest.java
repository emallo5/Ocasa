package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 07/04/16.
 */
public class RecordsTaskLoaderTest extends AsyncTaskLoader<List<CellViewModel>> {

    private List<CellViewModel> data;
    private long[] recordIds;

    public RecordsTaskLoaderTest(Context context, long[] recordIds) {
        super(context);
        this.recordIds = recordIds;
    }

    @Override
    public List<CellViewModel> loadInBackground() {

        List<CellViewModel> cells = new ArrayList<>();

        RecordDAO dao = new RecordDAO(getContext());

        ReceiptCacheManager cacheManager = ReceiptCacheManager.getInstance();

        if(recordIds == null)
            return cells;

        for (long recordId: recordIds) {
            Record record = dao.findById(recordId);
            CellViewModel cell = new CellViewModel();
            cell.setId(recordId);

            if(!cacheManager.recordExists(recordId)) {
                cacheManager.saveRecord(record);
            }

            cacheManager.fillRecord(record);
            fillCell(cell, record);
            cells.add(cell);
        }

        return cells;
    }

    private void fillCell(CellViewModel cell, Record record){

        List<Field> fields = new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(cell.getId()));

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        for (Field field : fields){
            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(record.findField(field.getId()).getValue());
            fieldViewModel.setLabel(field.getColumn().getName());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }

    @Override
    public void deliverResult(List<CellViewModel> data) {
        this.data = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(data != null)
            deliverResult(data);
        else{
            forceLoad();
        }
    }
}
