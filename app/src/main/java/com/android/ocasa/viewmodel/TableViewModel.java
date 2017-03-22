package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class TableViewModel {

    private FormViewModel header;

    private String id;

    private String name;

    private List<CellViewModel> cells;
    private ArrayList<String> orderBy = new ArrayList<>();

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addOrderColumn(String column) {
        orderBy.add(column);
    }

    public ArrayList<String> getOrderBy() {
        return orderBy;
    }
}
