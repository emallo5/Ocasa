package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Table;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class Menu {

    @SerializedName("Applications")
    private List<Application> applications;

    public Menu(){ }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public static class ActionDeserializer implements JsonDeserializer<Action> {

        @Override
        public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            Action action = new Action();
            JsonObject jObject = json.getAsJsonObject();


            action.setId(jObject.get("Id").getAsString());
            action.setName(jObject.get("Name").getAsString());

            Table table = new Table();
            table.setId(jObject.get("Table_id").getAsString());
            table.setType(jObject.has("Type") ? jObject.get("Type").getAsString() : "");

            action.setColumnsHeader(parserColumns(table.getId(), jObject.getAsJsonArray("Columns_header"), ColumnAction.ColumnActionType.HEADER));
            action.setColumnsDetail(parserColumns(table.getId(), jObject.getAsJsonArray("Columns_detail"), ColumnAction.ColumnActionType.DETAIL));

            action.setTable(table);

            return action;

        }

        private List<ColumnAction> parserColumns(String tableId, JsonArray columns, ColumnAction.ColumnActionType type){

            List<ColumnAction> columnsAction = new ArrayList<>();

            for (JsonElement jColumn: columns) {
                ColumnAction columnAction = new ColumnAction();
                columnAction.setType(type);

                JsonObject jObject = jColumn.getAsJsonObject();

                Column column = new Column();
                column.setId(tableId + "|" + jObject.get("Column_id").getAsString());

                columnAction.setColumn(column);

                columnAction.setEditable(!jObject.has("Editable") || jObject.get("Editable").getAsBoolean());
                columnAction.setDefaultValue(jObject.get("Default").isJsonNull() ? "" : jObject.get("Default").getAsString());
                columnAction.setLastValue(jObject.get("Last").isJsonNull() ? "" : jObject.get("Last").getAsString());

                columnsAction.add(columnAction);
            }

            return columnsAction;
        }
    }
}
