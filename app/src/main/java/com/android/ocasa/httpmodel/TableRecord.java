package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.Record;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 28/01/16.
 */
public class TableRecord {

    @SerializedName("Records")
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

            record.setExternalId(jObject.has("Id") ? jObject.get("Id").getAsString() : "");
            record.setStatus(jObject.has("status") ? jObject.get("status").getAsString() : "");
            record.setFields(parserFields(jObject.getAsJsonArray("Fields")));

            return record;

        }

        private List<Field> parserFields(JsonArray jFields){

            List<Field> fields = new ArrayList<>();

            for (JsonElement jField: jFields) {
                Field field = new Field();
                JsonObject jObject = jField.getAsJsonObject();

                field.setValue(jObject.get("Value").getAsString());

                Column column = new Column();
                column.setId(jObject.get("Column_id").getAsString());

                field.setColumn(column);

                fields.add(field);
            }

            return fields;
        }
    }

    public static class RecordSerializer implements JsonSerializer<Record>{

        @Override
        public JsonElement serialize(Record src, Type typeOfSrc, JsonSerializationContext context) {

            JsonObject jRecord = new JsonObject();

            jRecord.addProperty("Id", src.getExternalId());

            JsonArray jFields = new JsonArray();

            List<Field> fields = new ArrayList<>(src.getFields());

            for (int index = 0; index < fields.size(); index++){
                Field field = fields.get(index);

                JsonObject jField = new JsonObject();
                jField.addProperty("Column_id", field.getColumn().getId());
                jField.addProperty("Value", field.getValue());

                jFields.add(jField);
            }

            jRecord.add("Fields", jFields);

            return jRecord;
        }
    }
}

