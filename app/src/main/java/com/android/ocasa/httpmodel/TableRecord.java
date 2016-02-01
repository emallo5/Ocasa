package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Record;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
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

    public static class FieldDeserializer implements JsonDeserializer<Field> {

        @Override
        public Field deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            Field field = new Field();
            JsonObject jObject = json.getAsJsonObject();

            field.setValue(jObject.get("value").getAsString());

            Column column = new Column();
            column.setId(jObject.get("column_id").getAsString());

            field.setColumn(column);

            return field;

        }
    }
}
