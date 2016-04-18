package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 11/04/16.
 */
public class TableViewModel {

    private String name;

    private List<CellViewModel> cells;

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
}
