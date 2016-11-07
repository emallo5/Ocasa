package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Column;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Table;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by ignacio on 26/01/16.
 */
public class HttpTable {

    public static class ColumnDeserializer implements JsonDeserializer<Column> {

        @Override
        public Column deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            Column column = new Column();
            JsonObject jObject = json.getAsJsonObject();

            column.setId(jObject.get("Column_Id").getAsString());
            column.setName(jObject.get("Name").getAsString());
            column.setOrder(jObject.get("Order").getAsInt());
            column.setLength(jObject.has("Length") ? jObject.get("Length").getAsInt() : 0);
            column.setEditable(jObject.get("Editable").getAsBoolean());
            column.setMandatory(jObject.get("Mandatory").getAsBoolean());
            column.setPrimaryKey(jObject.has("Primary_Key") && jObject.get("Primary_Key").getAsBoolean());
            column.setLogic(jObject.has("Logic") && jObject.get("Logic").getAsBoolean());
            column.setVisible(jObject.has("Visible") && jObject.get("Visible").getAsBoolean());
            column.setFieldType(FieldType.findTypeByApiName(jObject.get("Field_Type").getAsString()));
            column.setHighlight(jObject.has("Highlight") && jObject.get("Highlight").getAsBoolean());
            column.setDefaultValue(jObject.has("Default") ? jObject.get("Default").getAsString() : "");

            if(column.isPrimaryKey() || column.getId().contains("cf_0200")){
                column.setLogic(true);
            }

            if(column.getFieldType() == FieldType.COMBO ||
                    column.getFieldType() == FieldType.LIST){

                Layout relationship = new Layout();
//                relationship.setExternalID("Combo|");
//
                Table table = new Table();
                table.setId(jObject.get("Table_id").getAsString());

                relationship.setTable(table);

                column.setRelationship(relationship);
            }

            return column;

        }
    }
}
