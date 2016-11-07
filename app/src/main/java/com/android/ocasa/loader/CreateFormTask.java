package com.android.ocasa.loader;

import android.content.Context;
import android.location.Location;

import com.android.ocasa.cache.dao.FieldDAO;
import com.android.ocasa.cache.dao.LayoutDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ignacio on 09/06/16.
 */
public class CreateFormTask extends SaveFormTask {

    public CreateFormTask(Context context) {
        super(context);
    }

    @Override
    protected Void doInBackground(FormData... data) {

        FormData formdata = data[0];

        createRecord(formdata.getValues(), formdata.getTableId(), formdata.getLastLocation());

        return null;
    }

    private Record createRecord(Map<String, String> formValues, String tableId, Location lastLocation){

        Layout layout = new LayoutDAO(context).findByExternalId(tableId);

        Table table = layout.getTable();

        Record record = new Record();
        record.setTable(table);

        FieldDAO dao = new FieldDAO(context);

        List<Field> fields = new ArrayList<>();

        for (Map.Entry<String, String> entry : formValues.entrySet()){

            Column column = new Column();
            column.setId(entry.getKey());

            Field field = new Field();
            field.setValue(entry.getValue());
            field.setColumn(column);
            field.setRecord(record);

            fields.add(field);
        }

        record.setFields(fields);
        record.fillConcatValues();

        new RecordDAO(context).save(record);
        dao.save(fields);

        return record;
    }
}
