package com.android.ocasa.loader;

import android.content.Context;

import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ActionRecordTaskLoader extends TableTaskLoader {

    public ActionRecordTaskLoader(Context context, String tableId, String query, long[] excludeIds) {
        super(context, tableId, query, excludeIds);
    }

    @Override
    public TableViewModel loadInBackground() {

        TableViewModel tableView = new TableViewModel();

        Action action = new ActionDAO(getContext()).findById(getTableId());

        tableView.setName(action.getName());

        action.setColumnsHeader(new ColumnActionDAO(getContext()).
                findColumnsForActionAndType(action.getId(), ColumnAction.ColumnActionType.HEADER));

        TableDAO tableDAO = new TableDAO(getContext());

        String tableId = action.getTable().getId();

        Table table = tableDAO.findById(tableId);

        if(table == null)
            return null;

        List<Record> records = new RecordDAO(getContext()).findForTableAndQuery(tableId, getQuery(), getExcludeIds());

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
}
