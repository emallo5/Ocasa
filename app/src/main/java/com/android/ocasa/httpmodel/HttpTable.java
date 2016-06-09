package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Table;
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
 * Created by ignacio on 26/01/16.
 */
public class HttpTable {

    private List<Column> columns;

    public HttpTable() { }

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
            column.setLength(jObject.has("length") ? jObject.get("length").getAsInt() : 0);
            column.setEditable(jObject.get("editable").getAsBoolean());
            column.setMandatory(jObject.get("mandatory").getAsBoolean());
            column.setPrimaryKey(jObject.has("primary_key") && jObject.get("primary_key").getAsBoolean());
            column.setLogic(jObject.has("logic") && jObject.get("logic").getAsBoolean());
            column.setFieldType(FieldType.findTypeByApiName(jObject.get("field_type").getAsString()));

            if(column.getFieldType() == FieldType.COMBO ||
                    column.getFieldType() == FieldType.LIST){
                Table table = new Table();
                table.setId(jObject.get("table_id").getAsString());

                column.setRelationship(table);
            }

            return column;

        }
    }
}
