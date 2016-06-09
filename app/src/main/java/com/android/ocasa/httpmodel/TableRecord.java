package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class TableRecord {

    private List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public static class RecordDeserializer implements JsonDeserializer<Record> {

        @Override
        public Record deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            Record record = new Record();

            JsonObject jObject = json.getAsJsonObject();

            record.setExternalId(jObject.has("id") ? jObject.get("id").getAsString() : "");
            record.setStatus(jObject.has("status") ? jObject.get("status").getAsString() : "");
            record.setFields(parserFields(jObject.getAsJsonArray("fields")));

            return record;

        }

        private List<Field> parserFields(JsonArray jFields){

            List<Field> fields = new ArrayList<>();

            for (JsonElement jField: jFields) {
                Field field = new Field();
                JsonObject jObject = jField.getAsJsonObject();

                field.setValue(jObject.get("value").getAsString());

                Column column = new Column();
                column.setId(jObject.get("column_id").getAsString());

                field.setColumn(column);

                fields.add(field);
            }

            return fields;
        }
    }
}
