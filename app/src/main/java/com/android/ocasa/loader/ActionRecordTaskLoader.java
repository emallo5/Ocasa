package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
 public class ActionRecordTaskLoader extends AsyncTaskLoader<TableViewModel> {

    private long receiptId;
    private String query;
    private long[] excludeIds;

    private TableViewModel table;

    public ActionRecordTaskLoader(Context context, long receiptId, String query, long[] excludeIds) {
        super(context);
        this.receiptId = receiptId;
        this.query = query;
        this.excludeIds = excludeIds;
    }

    @Override
    public TableViewModel loadInBackground() {

        TableViewModel tableView = new TableViewModel();

        Receipt receipt = new ReceiptDAO(getContext()).findById(receiptId);

        Action action = receipt.getAction();

        tableView.setName(action.getTable().getName());

        action.setColumnsHeader(new ColumnActionDAO(getContext()).
                findColumnsForActionAndType(action.getId(), ColumnAction.ColumnActionType.HEADER));

        TableDAO tableDAO = new TableDAO(getContext());

        String tableId = action.getTable().getId();

        Table table = tableDAO.findById(tableId);

        if(table == null)
            return null;

        List<Record> records = RecordDAO.getInstance(getContext()).findForTableAndQuery(tableId, query, excludeIds);

        for (Record record: filter(records, action)) {
            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());

            fillCell(cell, record.getLogicFields());
            tableView.addCell(cell);
        }

        return tableView;
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
