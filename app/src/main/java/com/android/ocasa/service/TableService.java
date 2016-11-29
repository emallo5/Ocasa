package com.android.ocasa.service;

import android.content.Context;
import android.util.Log;

import com.android.ocasa.cache.dao.ApplicationDAO;
import com.android.ocasa.cache.dao.CategoryDAO;
import com.android.ocasa.cache.dao.ColumnDAO;
import com.android.ocasa.cache.dao.LayoutColumnDAO;
import com.android.ocasa.cache.dao.LayoutDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.cache.dao.TableDAO;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 26/01/16.
 */
public class TableService {

    static final String TAG = "TableService";

    public TableService(){
    }

    public Table findTable(Context context, String tableId){
        return new TableDAO(context).findById(tableId);
    }

    public FormViewModel getFormForTable(Context context, String tableId){

        FormViewModel form = new FormViewModel();

        Table table = findTable(context, tableId);

        Category category = table.getCategory();

//        if(category == null){
//            Receipt receipt = new ReceiptDAO(context).findRecord(receiptId);
//            category = new CategoryDAO(context).findRecord(receipt.getAction().getCategory().getId());
//        }else{
//            category = new CategoryDAO(context).findRecord(category.getId());
//        }

//        Application application = new ApplicationDAO(context).findRecord(category.getApplication().getId());

        form.setTitle(tableId);
        form.setColor("#33BDC2");

        return form;
    }

    public TableViewModel getRecords(Context context, String layoutId, String query, long[] excludeIds){

        TableViewModel table = getTableViewModel(context, layoutId);

        List<Record> records = new RecordDAO(context).findForTableAndQuery(table.getId(), query, excludeIds);

        fillTable(table, records);

        return table;
    }

    public TableViewModel getTableViewModel(Context context, String layoutId){

        TableViewModel tableViewModel = new TableViewModel();

        Layout layout = new LayoutDAO(context).findByExternalId(layoutId);

        if(layout == null)
            return null;

        Category category = new CategoryDAO(context).findById(layout.getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        FormViewModel header = new FormViewModel();

        Table table = layout.getTable();

        FieldViewModel headerField = new FieldViewModel();
        headerField.setValue(table.getFilters());

        header.addField(headerField);

        tableViewModel.setId(table.getId());
        tableViewModel.setCanAddNewRecord(table.canAddNewRecord());
        tableViewModel.setHeader(header);
        tableViewModel.setColor(application.getRecordColor());
        tableViewModel.setName(table.getName());

        return tableViewModel;
    }

    public void fillTable(TableViewModel table, List<Record> records){

        for (int index = 0; index < records.size(); index++){

            Record record = records.get(index);

            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());
            cell.setNew(record.isNew());
            cell.setUpdated(record.isUpdated());
            cell.setValue(record.getPrimaryKey().getValue());

            fillCell(cell, record.getVisibleFields());
            table.addCell(cell);
        }
    }

    public void fillCell(CellViewModel cell, List<Field> fields){

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        for (Field field : fields){
            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());
            fieldViewModel.setTag(field.getColumn().getId());
            fieldViewModel.setLabel(field.getColumn().getName());
            fieldViewModel.setPrimaryKey(field.getColumn().isPrimaryKey());
            fieldViewModel.setHighlight(field.getColumn().isHighlight());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }

    public void saveTable(Context context, Layout response){

        if(response.getExternalID() == null){
            return;
        }

        LayoutDAO layoutDAO = new LayoutDAO(context);
        LayoutColumnDAO layoutColumnDAO = new LayoutColumnDAO(context);
        layoutColumnDAO.deleteForLayout(response.getExternalID());
        TableDAO tableDAO = new TableDAO(context);

        Layout layout = new Layout();
        layout.setExternalID(response.getExternalID());
        layout.setColumns(response.getColumns());

        Table table = response.getTable();

        layout.setTable(table);
        layoutDAO.save(layout);

        tableDAO.update(table);

        Log.v(TAG, "Save Table " + layout.getExternalID());

        ColumnDAO dao = new ColumnDAO(context);

        List<Column> columns = new ArrayList<>();

        if(layout.getTable().getId().equalsIgnoreCase("OM_MovilNovedad")){

            Column sigature = new Column();
            sigature.setId("OM_MovilNovedad_cf_0400");
            sigature.setName("Firma");
            sigature.setVisible(false);
            sigature.setLogic(true);
            sigature.setOrder(9998);
            sigature.setFieldType(FieldType.SIGNATURE);

            LayoutColumn layoutColumn = new LayoutColumn(layout, sigature);
            sigature.addLayout(layoutColumn);

            layout.getColumns().add(layoutColumn);

            Column photo = new Column();
            photo.setId("OM_MovilNovedad_cf_0500");
            photo.setName("Foto");
            photo.setVisible(false);
            photo.setLogic(true);
            photo.setOrder(9999);
            photo.setFieldType(FieldType.PHOTO);

            LayoutColumn photoColumn = new LayoutColumn(layout, photo);
            photo.addLayout(photoColumn);

            layout.getColumns().add(photoColumn);

            Column photo1 = new Column();
            photo1.setId("OM_MovilNovedad_cf_0600");
            photo1.setName("Foto 2");
            photo1.setVisible(false);
            photo1.setLogic(true);
            photo1.setOrder(10000);
            photo1.setFieldType(FieldType.PHOTO);

            LayoutColumn photoColumn1 = new LayoutColumn(layout, photo1);
            photo1.addLayout(photoColumn1);

            layout.getColumns().add(photoColumn1);

            Column photo2 = new Column();
            photo2.setId("OM_MovilNovedad_cf_0700");
            photo2.setName("Foto 3");
            photo2.setVisible(false);
            photo2.setLogic(true);
            photo2.setOrder(10001);
            photo2.setFieldType(FieldType.PHOTO);

            LayoutColumn photoColumn2 = new LayoutColumn(layout, photo2);
            photo2.addLayout(photoColumn2);

            layout.getColumns().add(photoColumn2);

            Column photo3 = new Column();
            photo3.setId("OM_MovilNovedad_cf_0800");
            photo3.setName("Foto 4");
            photo3.setVisible(false);
            photo3.setLogic(true);
            photo3.setOrder(10002);
            photo3.setFieldType(FieldType.PHOTO);

            LayoutColumn photoColumn3 = new LayoutColumn(layout, photo3);
            photo3.addLayout(photoColumn3);

            layout.getColumns().add(photoColumn3);
        }

        for (LayoutColumn layoutColumn : layout.getColumns()){

            layoutColumn.setLayout(layout);
            Column column = layoutColumn.getColumn();

            column.addLayout(layoutColumn);

            columns.add(column);

            if(column.isCombo() ||
                    column.getFieldType() == FieldType.LIST){
                layoutDAO.save(column.getRelationship());

                tableDAO.save(column.getRelationship().getTable());
            }
        }

        dao.save(columns);
        layoutColumnDAO.save(layout.getColumns());

        new RecordService().saveRecordsFromTable(context, response.getTable());
    }
}
