package com.android.ocasa.httpmodel;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 07/11/16.
 */

public class MediaBody {

    @SerializedName("CollArchive")
    private List<RecordArchive> record;

    public List<RecordArchive> getRecord() {
        return record;
    }

    public void setRecord(List<RecordArchive> record) {
        this.record = record;
    }

    public void addRecordArchive(RecordArchive archive){
        if(record == null){
            record = new ArrayList<>();
        }

        record.add(archive);
    }
}
