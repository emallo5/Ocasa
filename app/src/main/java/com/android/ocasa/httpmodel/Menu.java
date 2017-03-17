package com.android.ocasa.httpmodel;

import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
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

    public static class CategoryDeserializer implements JsonDeserializer<Category>{

        @Override
        public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            JsonObject jObject = json.getAsJsonObject();

            Category category = new Category();
            category.setId(jObject.get("Id").getAsString());
            category.setName(jObject.get("Name").getAsString());
            category.setVisible(jObject.get("Visible").getAsBoolean());

            category.setLayouts(parseLayouts(category, jObject.getAsJsonArray("Tables")));
            category.setActions(parseActions(category, jObject.getAsJsonArray("Actions")));

            return category;
        }

        private List<Layout> parseLayouts(Category category, JsonArray tables){

            List<Layout> layouts = new ArrayList<>();

            for (int index = 0; index < tables.size(); index++) {

                JsonObject jTable = tables.get(index).getAsJsonObject();

                Layout layout = new Layout();
                layout.setExternalID(jTable.get("Layout").getAsString());
                layout.setCategory(category);

                Table table = new Table();
                table.setId(jTable.get("Id").getAsString());
                table.setName(jTable.get("Name").getAsString());
                table.setType(jTable.get("Type").getAsString());

                layout.setTable(table);
                layouts.add(layout);
            }

            return layouts;
        }

        private List<Action> parseActions(Category category, JsonArray jActions){

            List<Action> actions = new ArrayList<>();

            for (int index = 0; index < jActions.size(); index++) {

                JsonObject jAction = jActions.get(index).getAsJsonObject();

                Action action = new Action();
                action.setId(jAction.get("Id").getAsString());
                action.setName(jAction.get("Name").getAsString());
                action.setCategory(category);

                Table table = new Table();
                table.setId(jAction.get("Table_id").getAsString());
                table.setType(jAction.has("Type") ? jAction.get("Type").getAsString() : "");

                action.setColumnsHeader(parserColumns(action.getId(), jAction.getAsJsonArray("Columns_header"), ColumnAction.ColumnActionType.HEADER));
                action.setColumnsDetail(parserColumns(action.getId(), jAction.getAsJsonArray("Columns_detail"), ColumnAction.ColumnActionType.DETAIL));

                action.setTable(table);

                actions.add(action);
            }

            return actions;
        }

        private List<ColumnAction> parserColumns(String actionId, JsonArray columns, ColumnAction.ColumnActionType type){

            List<ColumnAction> columnsAction = new ArrayList<>();

            for (JsonElement jColumn: columns) {
                ColumnAction columnAction = new ColumnAction();
                columnAction.setType(type);

                JsonObject jObject = jColumn.getAsJsonObject();

                Column column = new Column();
                column.setId(jObject.get("Column_id").getAsString());

                columnAction.setColumn(column);

                columnAction.setEditable(!jObject.has("Editable") || jObject.get("Editable").getAsBoolean());
                columnAction.setDefaultValue(jObject.get("Default").isJsonNull() ? "" : jObject.get("Default").getAsString());
                columnAction.setLastValue(jObject.get("Last").isJsonNull() ? "" : jObject.get("Last").getAsString());

                columnsAction.add(columnAction);
            }

            return columnsAction;
        }
    }

//    public static class TableDeserializer implements JsonDeserializer<Table>{
//
//        @Override
//        public Table deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            if (json == null)
//                return null;
//
//            JsonObject jObject = json.getAsJsonObject();
//
//            Table table = new Table();
//            table.setId(jObject.get("Id").getAsString());
//            table.setName(jObject.get("Name").getAsString());
//
//            Layout layout = new Layout();
//            layout.setId(jObject.get("Layout").getAsString());
//            layout.setTable(table);
//
//            return table;
//        }
//    }


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

    public static class LayoutDeserializer implements JsonDeserializer<Layout>{

        @Override
        public Layout deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null)
                return null;

            JsonObject jTable = json.getAsJsonObject();

            Layout layout = new Layout();
            layout.setExternalID(jTable.get("Layout").getAsString());

            Table table = new Table();
            table.setId(jTable.get("Id").getAsString());
            table.setName(jTable.get("Name").getAsString());

            layout.setTable(table);

            layout.setColumns(parseColumns(layout, jTable.getAsJsonArray("Columns")));

            return layout;
        }

        private List<LayoutColumn> parseColumns(Layout layout, JsonArray jColumns){

            List<LayoutColumn> columns = new ArrayList<>();

            for (int index = 0; index < jColumns.size(); index++) {

                Column column = new Column();

                LayoutColumn layoutColumn = new LayoutColumn(layout, column);

                column.addLayout(layoutColumn);
                JsonObject jObject = jColumns.get(index).getAsJsonObject();

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
                column.setDetail(jObject.has("Detail") && jObject.get("Detail").getAsBoolean());

                if(column.isPrimaryKey() || column.getId().contains("CF_0200")){
                    column.setLogic(true);
                }

                if(column.getFieldType() == FieldType.COMBO ||
                        column.getFieldType() == FieldType.LIST){
                    Layout relationship = new Layout();
                    relationship.setExternalID(layout.getExternalID());

                    Table table = new Table();
                    table.setId(jObject.get("Table_id").getAsString());

                    relationship.setTable(table);

                    column.setRelationship(relationship);
                }

                columns.add(layoutColumn);
            }

            return columns;
        }
    }
}
