package com.android.ocasa.service;

import android.content.Context;
import android.util.Log;

import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 26/01/16.
 */
public class TableService {

    static final String TAG = "TableService";

    public TableService(){
    }

    public TableViewModel getRecords(Context context, String tableId, String query, long[] excludeIds){

        TableViewModel table = getTableViewModel(context, tableId);

        List<Record> records = RecordDAO.getInstance(context).findForTableAndQuery(tableId, query, excludeIds);

        fillTable(table, records);

        return table;
    }

    public TableViewModel getTableViewModel(Context context, String tableId){
        TableViewModel tableViewModel = new TableViewModel();

        Table table = new TableDAO(context).findById(tableId);

        if(table == null)
            return null;

        Category category = new CategoryDAO(context).findById(table.getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        FormViewModel header = new FormViewModel();

        FieldViewModel headerField = new FieldViewModel();
        headerField.setValue(table.getFilters());

        header.addField(headerField);

        tableViewModel.setCanAddNewRecord(table.canAddNewRecord());
        tableViewModel.setHeader(header);
        tableViewModel.setColor(application.getRecordColor());
        tableViewModel.setName(table.getName());

        return tableViewModel;
    }

    public void fillTable(TableViewModel table, List<Record> records){

        for (int index = 0; index < records.size(); index++){

            Record record = records.get(index);

            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());
            cell.setNew(record.isNew());
            cell.setUpdated(record.isUpdated());
            cell.setValue(record.getPrimaryKey().getValue());

            fillCell(cell, record.getVisibleFields());
            table.addCell(cell);
        }
    }

    public void fillCell(CellViewModel cell, List<Field> fields){

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        for (Field field : fields){
            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());
            fieldViewModel.setTag(field.getColumn().getId());
            fieldViewModel.setLabel(field.getColumn().getName());
            fieldViewModel.setPrimaryKey(field.getColumn().isPrimaryKey());
            fieldViewModel.setHighlight(field.getColumn().isHighlight());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }

    public void saveTable(Context context, Table httpTable){

        if(httpTable.getId() == null){
            return;
        }

        TableDAO tableDAO = new TableDAO(context);

        Table table = tableDAO.findById(httpTable.getId());

        if(table == null){
            table = new Table();
            table.setId(httpTable.getId());
        }

        table.setColumns(httpTable.getColumns());
        table.setName(httpTable.getName());

        tableDAO.save(table);

        Log.v(TAG, "Save Table " + table.getId() + ", " + table.getName());

        ColumnDAO dao = new ColumnDAO(context);

        for (Column column : table.getColumns()){
            column.setTable(table);
            column.setId(table.getId() + "|" + column.getId());

            if(column.getFieldType() == FieldType.COMBO ||
                    column.getFieldType() == FieldType.LIST){
                tableDAO.save(column.getRelationship());
            }
        }

        dao.save(table.getColumns());

        RecordDAO recordDAO = RecordDAO.getInstance(context);
        HistoryDAO historyDAO = new HistoryDAO(context);

        FieldDAO fieldDAO = new FieldDAO(context);

        List<Record> records = new ArrayList<>(httpTable.getRecords());

        List<Field> fields = new ArrayList<>();
        List<History> histories = new ArrayList<>();

        for (int index = 0; index <  records.size(); index++){

            Record record = records.get(index);

            record.setExternalId(table.getId() + "|" + record.getExternalId());
            record.setTable(table);
            record.fillConcatValues();

            Record updated = recordDAO.findByExternalId(record.getExternalId());
            if(updated != null) {
                record.setId(updated.getId());

                for (Field field : record.getFields()){

                    field.getColumn().setId(table.getId() + "|" + field.getColumn().getId());

                    Field updateField = updated.getFieldForColumn(field.getColumn().getId());

                    if(updateField != null && !field.getValue().equalsIgnoreCase(updateField.getValue())) {

                        if(!updateField.getValue().equalsIgnoreCase(field.getValue())) {
                            History history = new History();
                            history.setValue(field.getValue());
                            history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
                            history.setField(updateField);
                            history.setTimeZone(DateTimeHelper.getDeviceTimezone());

                            updateField.addHistory(history);

                            histories.add(history);
                        }

                        fields.add(updateField);
                        updateField.setValue(field.getValue());
                    }
                }

            }else{
                for (Field field : record.getFields()){
                    field.setRecord(record);

                    field.getColumn().setId(table.getId() + "|" + field.getColumn().getId());

                    History history = new History();
                    history.setValue(field.getValue());
                    history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
                    history.setField(field);
                    history.setTimeZone(DateTimeHelper.getDeviceTimezone());

                    field.addHistory(history);

                    fields.add(field);
                    histories.add(history);
                }
            }
        }

        recordDAO.save(records);
        fieldDAO.save(fields);
        historyDAO.save(histories);
    }
}
