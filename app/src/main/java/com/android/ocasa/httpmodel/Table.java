package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.FieldType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ignacio on 26/01/16.
 */
public class Table {

    private List<Column> columns;

    public Table() { }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public static class ColumnDeserializer implements JsonDeserializer<Column> {

        @Override
        public Column deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            Column column = new Column();
            JsonObject jObject = json.getAsJsonObject();

            column.setId(jObject.get("column_id").getAsString());
            column.setName(jObject.get("name").getAsString());
            column.setOrder(jObject.get("order").getAsInt());
            column.setLength(jObject.get("length").getAsInt());
            column.setEditable(jObject.get("editable").getAsBoolean());
            column.setMandatory(jObject.get("mandatory").getAsBoolean());
            column.setFieldType(FieldType.findTypeByApiName(jObject.get("field_type").getAsString()));

            return column;

        }
    }
}
