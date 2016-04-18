package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class TableTaskLoader extends AsyncTaskLoader<TableViewModel> {

    private String tableId;
    private String query;
    private long[] excludeIds;

    private TableViewModel table;

    public TableTaskLoader(Context context, String tableId, String query) {
        this(context, tableId, query, null);
    }

    public TableTaskLoader(Context context, String tableId, String query, long[] excludeIds) {
        super(context);
        this.tableId = tableId;
        this.query = query;
        this.excludeIds = excludeIds;
    }

    @Override
    public TableViewModel loadInBackground() {

        TableViewModel tableViewModel = new TableViewModel();

        Table table = new TableDAO(getContext()).findById(tableId);

        if(table == null)
            return null;

        tableViewModel.setName(table.getName());

        List<Record> records = new RecordDAO(getContext()).findForTableAndQuery(tableId, query, excludeIds);

        for (Record record: records) {
            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());
            cell.setValue(record.getPrimaryKey().getValue());

            fillCell(cell, record.getLogicFields());
            tableViewModel.addCell(cell);
        }

        return tableViewModel;
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

    private List<Record> filter(List<Record> records){

        List<Record> filterRecords = new ArrayList<>();

        filterRecords.addAll(applySubFilter(records, "2", "Test"));

        List<Record> aux = applySubFilter(filterRecords, "4", "test");

        filterRecords.clear();
        filterRecords.addAll(aux);

        aux = applySubFilter(filterRecords, "7", "23:45");

        filterRecords.clear();
        filterRecords.addAll(aux);

        return filterRecords;
    }

    public List<Record> applySubFilter(List<Record> records, String columnId, String value){

        List<Record> subList = new ArrayList<>();

        for (Record record : records){

            Field field = record.getFieldForColumn(columnId);

            if(field.getValue().contains(value)){
                subList.add(record);
            }
        }

        return subList;
    }

    public String getTableId() {
        return tableId;
    }

    public String getQuery() {
        return query;
    }

    public long[] getExcludeIds() {
        return excludeIds;
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
