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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 18/01/16.
 */
public class Menu {

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

            action.setId(jObject.get("id").getAsString());
            action.setName(jObject.get("name").getAsString());

            action.setColumnsHeader(parserColumns(jObject.getAsJsonArray("columns_header"), ColumnAction.ColumnActionType.HEADER));
            action.setColumnsDetail(parserColumns(jObject.getAsJsonArray("columns_detail"), ColumnAction.ColumnActionType.DETAIL));

            Table table = new Table();
            table.setId(jObject.get("table_id").getAsString());

            action.setTable(table);

            return action;

        }

        private List<ColumnAction> parserColumns(JsonArray columns, ColumnAction.ColumnActionType type){

            List<ColumnAction> columnsAction = new ArrayList<>();

            for (JsonElement jColumn: columns) {
                ColumnAction columnAction = new ColumnAction();
                columnAction.setType(type);

                JsonObject jObject = jColumn.getAsJsonObject();

                Column column = new Column();
                column.setId(jObject.get("column_id").getAsString());

                columnAction.setColumn(column);

                columnAction.setEditable(!jObject.has("editable") || jObject.get("editable").getAsBoolean());
                columnAction.setDefaultValue(jObject.get("default").getAsString());
                columnAction.setLastValue(jObject.get("last").getAsString());

                columnsAction.add(columnAction);
            }

            return columnsAction;
        }
    }
}
