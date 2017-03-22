package com.android.ocasa.viewmodel;

import com.android.ocasa.model.Field;

import java.util.List;

/**
 * Ignacio Oviedo on 07/04/16.
 */
public class CellViewModel {

    private long id;

    private int number;

    private String value;

    private List<FieldViewModel> fields;

    private boolean isNew;

    private boolean isUpdated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<FieldViewModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldViewModel> fields) {
        this.fields = fields;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public String getKeyValue() {
        for (int index = 0; index < fields.size(); index++) {
            FieldViewModel field = fields.get(index);
            if(field.isPrimaryKey()){
                return field.getValue();
            }
        }

        return null;
    }

    public String getColumnValue(String column) {
        for (FieldViewModel field : fields) {
            if(field.getTag().equals(column)) {
                return field.getValue();
            }
        }

        return null;
    }
}
