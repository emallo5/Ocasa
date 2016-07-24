package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DateTimeHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 26/01/16.
 */
public class TableService {

    public TableService(){
    }

    public void saveTable(Context context, HttpTable httpTable){
        TableDAO tableDAO = new TableDAO(context);

        Table table = tableDAO.findById(httpTable.getTable().getId());

        if(table == null){
            table = new Table();
            table.setId(httpTable.getTable().getId());
        }

        table.setColumns(httpTable.getTable().getColumns());
        table.setName(httpTable.getTable().getName());

        tableDAO.save(table);

        ColumnDAO dao = new ColumnDAO(context);

        for (Column column : table.getColumns()){
            column.setTable(table);

            if(column.getFieldType() == FieldType.COMBO ||
                    column.getFieldType() == FieldType.LIST){
                tableDAO.save(column.getRelationship());
            }
        }

        dao.save(table.getColumns());

        RecordDAO recordDAO = RecordDAO.getInstance(context);
        HistoryDAO historyDAO = new HistoryDAO(context);

        FieldDAO fieldDAO = new FieldDAO(context);

        List<Record> records = new ArrayList<>(httpTable.getTable().getRecords());

        List<Field> fields = new ArrayList<>();
        List<History> histories = new ArrayList<>();

        for (int index = 0; index <  records.size(); index++){

            Record record = records.get(index);

            record.setTable(table);
            record.fillConcatValues();

            Record updated = recordDAO.findByExternalId(record.getExternalId());
            if(updated != null) {
                record.setId(updated.getId());
                fieldDAO.deleteFieldsForRecord((int) record.getId());
            }

            for (Field field : record.getFields()){
                field.setRecord(record);

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

        recordDAO.save(records);
        fieldDAO.save(fields);
        historyDAO.save(histories);
    }


}
