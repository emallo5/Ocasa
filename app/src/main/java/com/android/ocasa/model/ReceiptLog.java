package com.android.ocasa.model;

/**
 * Created by ignacio on 07/02/17.
 */

public class ReceiptLog {

    private String id;
    private String uploadImage;
    private String uploadData;


    public ReceiptLog (String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }

    public String getUploadData() {
        return uploadData;
    }

    public void setUploadData(String uploadData) {
        this.uploadData = uploadData;
    }
}