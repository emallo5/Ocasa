package com.android.ocasa.viewmodel;

import com.android.ocasa.model.FieldType;

import java.util.List;

/**
 * Created by Emiliano Mallo on 04/04/16.
 */
public class FieldViewModel {

    private String tag;

    private String label;

    private String value;

    private FieldType type;

    private String relationshipTable;

    private boolean isEditable;

    private boolean isPrimaryKey;

    private List<FieldViewModel> relationshipFields;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getRelationshipTable() {
        return relationshipTable;
    }

    public void setRelationshipTable(String relationshipTable) {
        this.relationshipTable = relationshipTable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public List<FieldViewModel> getRelationshipFields() {
        return relationshipFields;
    }

    public void setRelationshipFields(List<FieldViewModel> relationshipFields) {
        this.relationshipFields = relationshipFields;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }
}
