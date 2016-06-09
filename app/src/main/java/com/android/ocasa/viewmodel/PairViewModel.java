package com.android.ocasa.viewmodel;

/**
 * Created by ignacio on 30/05/16.
 */
public class PairViewModel {

    private FieldViewModel firstField;
    private FieldViewModel secondField;

    public FieldViewModel getFirstField() {
        return firstField;
    }

    public void setFirstField(FieldViewModel firstField) {
        this.firstField = firstField;
    }

    public FieldViewModel getSecondField() {
        return secondField;
    }

    public void setSecondField(FieldViewModel secondField) {
        this.secondField = secondField;
    }
}
