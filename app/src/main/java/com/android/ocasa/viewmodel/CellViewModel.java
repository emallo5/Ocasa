package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 07/04/16.
 */
public class CellViewModel {

    private long id;

    private String value;

    private List<FieldViewModel> fields;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

}
