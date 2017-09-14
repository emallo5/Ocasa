package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by leandro on 5/7/17.
 */

public class PodStructuresById {

    @SerializedName("id")
    private String id;

    @SerializedName("columns")
    private List<VisibleColumn> columns;


    public boolean containsColumn(String columnId) {
        for (VisibleColumn column : columns)
            if (column.getId().equalsIgnoreCase(columnId)) return true;
        return false;
    }

    public VisibleColumn getColumn(String columnId) {
        for (VisibleColumn column : columns)
            if (column.getId().equalsIgnoreCase(columnId)) return column;
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<VisibleColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<VisibleColumn> columns) {
        this.columns = columns;
    }

    public class VisibleColumn {

        @SerializedName("id")
        private String id;

        @SerializedName("label")
        private String name;

        @SerializedName("mandatory")
        private boolean mandatory;

        @SerializedName("default")
        private String defaultValue;

        @SerializedName("rules")
        private List<Rule> rules;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaults) {
            this.defaultValue = defaults;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void setRules(List<Rule> rules) {
            this.rules = rules;
        }
    }

    public class Rule {

        @SerializedName("id")
        private String id;

        @SerializedName("operator")
        private String operator;

        @SerializedName("value")
        private String value;

        @SerializedName("nextcondition")
        private String next;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }
    }
}