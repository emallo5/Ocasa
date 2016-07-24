package com.android.ocasa.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Ignacio Oviedo on 04/04/16.
 */
public class FormViewModel {

    private long id;

    private String title;

    private List<FieldViewModel> fields;

    private List<PairViewModel> status;

    private String color;

    public FormViewModel(){
        this.fields = new ArrayList<>();
        this.status = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addField(FieldViewModel field){
        this.fields.add(field);
    }

    public List<FieldViewModel> getFields() {
        return fields;
    }

    public FieldViewModel getFieldForTag(String tag){
        for (FieldViewModel field : fields){
            if(field.getTag().equalsIgnoreCase(tag))
                return field;
        }
        return null;
    }

    public void addPair(PairViewModel field){
        this.status.add(field);
    }

    public List<PairViewModel> getStatus() {
        return status;
    }

    public void setStatus(List<PairViewModel> status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
