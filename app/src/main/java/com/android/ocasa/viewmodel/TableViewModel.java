package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class TableViewModel {

    private FormViewModel header;

    private String name;

    private List<CellViewModel> cells;

    private String color;

    private boolean canAddNewRecord;

    public TableViewModel(){
        cells = new ArrayList<>();
    }

    public void addCell(CellViewModel cell){
        cells.add(cell);
    }

    public List<CellViewModel> getCells(){
        return cells;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public FormViewModel getHeader() {
        return header;
    }

    public void setHeader(FormViewModel header) {
        this.header = header;
    }

    public boolean canAddNewRecord() {
        return canAddNewRecord;
    }

    public void setCanAddNewRecord(boolean canAddNewRecord) {
        this.canAddNewRecord = canAddNewRecord;
    }
}
