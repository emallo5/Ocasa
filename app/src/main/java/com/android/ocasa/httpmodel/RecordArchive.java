package com.android.ocasa.httpmodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 07/11/16.
 */

public class RecordArchive {

    @SerializedName("Id")
    private String id;

    @SerializedName("Archive")
    private List<Archive> archives;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Archive> getArchives() {
        return archives;
    }

    public void setArchives(List<Archive> archives) {
        this.archives = archives;
    }

    public void addArchive(Archive archive){
        if(archives == null){
            archives = new ArrayList<>();
        }

        archives.add(archive);
    }
}
