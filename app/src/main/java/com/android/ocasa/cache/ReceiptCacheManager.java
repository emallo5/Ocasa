package com.android.ocasa.cache;

import com.android.ocasa.model.Field;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 31/03/16.
 */
public class ReceiptCacheManager {

    private static ReceiptCacheManager instance;

    static final String CACHE_NAME = "receipt_cache";

    private DualCache<List> cache;

    public static ReceiptCacheManager getInstance() {

        if(instance == null)
            instance = new ReceiptCacheManager();

        return instance;
    }

    private ReceiptCacheManager(){
        cache = new DualCacheBuilder<>(CACHE_NAME, 1, List.class)
                .useDefaultSerializerInRam(1000)
                .useDefaultSerializerInDisk(1000, true);
    }

    public boolean recordExists(long recordId){
        return cache != null && cache.get(String.valueOf(recordId)) != null;
    }

    public void fillRecord(Record record){

        List<FieldInfo> fieldInfos = (List<FieldInfo>) cache.get(String.valueOf(record.getId()));

        if(fieldInfos == null)
            return;

        for (FieldInfo fieldInfo : fieldInfos){
            Field field = record.findField(fieldInfo.getId());

            if(field != null)
                field.setValue(fieldInfo.getValue());
        }
    }

    public void fillReceipt(Receipt receipt){

        List<FieldInfo> fieldInfos = (List<FieldInfo>) cache.get("Receipt");

        List<Field> headerValues = new ArrayList<>();

        for (FieldInfo fieldInfo : fieldInfos){
            Field field = new Field();
            field.setId((int) fieldInfo.getId());
            field.setValue(fieldInfo.getValue());

            headerValues.add(field);
        }

        receipt.setHeaderValues(headerValues);
    }

    public void saveRecord(Record record){

        List<FieldInfo> values = new ArrayList<>();

        for (Field field : record.getFields()){

            FieldInfo fieldInfo = new FieldInfo(field.getId(), field.getValue());

            values.add(fieldInfo);
        }

        cache.put(String.valueOf(record.getId()), values);
    }

    public void saveReceipt(Receipt record){

        List<FieldInfo> values = new ArrayList<>();

        for (Field field : record.getHeaderValues()){

            FieldInfo fieldInfo = new FieldInfo(0, field.getValue());
            fieldInfo.setColumnId(field.getColumn().getId());

            values.add(fieldInfo);
        }

        cache.put("Receipt", values);
    }


    public void clearCache(){
        cache.invalidate();
    }

    public static class FieldInfo{
        private long id;
        private String columnId;
        private String value;

        public FieldInfo(){}

        public FieldInfo(long id, String value) {
            this.id = id;
            this.value = value;
        }

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

        public String getColumnId() {
            return columnId;
        }

        public void setColumnId(String columnId) {
            this.columnId = columnId;
        }
    }
}
