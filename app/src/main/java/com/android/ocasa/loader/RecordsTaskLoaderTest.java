package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Receipt;
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
    private long receiptId;

    public RecordsTaskLoaderTest(Context context, long receiptId, long[] recordIds) {
        super(context);
        this.receiptId = receiptId;
        this.recordIds = recordIds;
    }

    @Override
    public List<CellViewModel> loadInBackground() {

        List<CellViewModel> cells = new ArrayList<>();

        Receipt receipt = new ReceiptDAO(getContext()).findById(receiptId);

        Action action = receipt.getAction();

        RecordDAO dao = RecordDAO.getInstance(getContext());

        ReceiptCacheManager cacheManager = ReceiptCacheManager.getInstance();

//        if(recordIds == null)
//            return cells;

        List<Record> records = new ArrayList<>();

        for (long recordId: recordIds) {
            Record record = dao.findById(recordId);

            records.add(record);
//            CellViewModel cell = new CellViewModel();
//            cell.setId(recordId);
//
//            if(!cacheManager.recordExists(recordId)) {
//                cacheManager.saveRecord(record);
//            }
//
//            cacheManager.fillRecord(record);
//            fillCell(cell, record);
//            cells.add(cell);
        }


        for (Record record: filter(records, action)) {
            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());

            fillCell(cell, record.getLogicFields());
            cells.add(cell);
        }


        return cells;
    }

    private List<Record> filter(List<Record> records, Action action){

        List<Record> filterRecords = new ArrayList<>();
        filterRecords.addAll(records);

        for (ColumnAction columnAction : action.getColumnsHeader()){
            if(columnAction.getLastValue() != null){
                List<Record> aux = applySubFilter(filterRecords, columnAction.getColumn().getId(), columnAction.getLastValue());
                filterRecords.clear();
                filterRecords.addAll(aux);
            }
        }

        return filterRecords;
    }

    public void fillCell(CellViewModel cell, List<Field> fields){

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        for (Field field : fields){
            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());
            fieldViewModel.setTag(field.getColumn().getId());
            fieldViewModel.setLabel(field.getColumn().getName());
            fieldViewModel.setPrimaryKey(field.getColumn().isPrimaryKey());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }

    public List<Record> applySubFilter(List<Record> records, String columnId, String value){

        List<Record> subList = new ArrayList<>();

        for (Record record : records){

            Field field = record.getFieldForColumn(columnId);

            if(field != null && field.getValue().contains(value)){
                subList.add(record);
            }
        }

        return subList;
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
