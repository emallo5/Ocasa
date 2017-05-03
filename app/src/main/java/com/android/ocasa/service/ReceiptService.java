package com.android.ocasa.service;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.ocasa.OcasaApplication;
import com.android.ocasa.cache.ReceiptCacheManager;
import com.android.ocasa.cache.dao.ActionDAO;
import com.android.ocasa.cache.dao.ApplicationDAO;
import com.android.ocasa.cache.dao.CategoryDAO;
import com.android.ocasa.cache.dao.ColumnActionDAO;
import com.android.ocasa.cache.dao.ColumnDAO;
import com.android.ocasa.cache.dao.FieldDAO;
import com.android.ocasa.cache.dao.HistoryDAO;
import com.android.ocasa.cache.dao.ReceiptDAO;
import com.android.ocasa.cache.dao.ReceiptItemDAO;
import com.android.ocasa.cache.dao.RecordDAO;
import com.android.ocasa.cache.dao.StatusDAO;
import com.android.ocasa.cache.dao.TableDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Status;
import com.android.ocasa.model.Table;
import com.android.ocasa.util.DateTimeHelper;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.PairViewModel;
import com.android.ocasa.viewmodel.ReceiptCellViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by ignacio on 14/07/16.
 */
public class ReceiptService{

    public ReceiptService(){
    }

    public Receipt findReceiptById(Context context, long receiptId){
        return new ReceiptDAO(context).findById(receiptId);
    }

    public void updateReceipt(Context context, Receipt receipt){
        new ReceiptDAO(context).update(receipt);
    }

    public ReceiptFormViewModel getHeaderByReceipt(Context context, long receiptId){
        ReceiptFormViewModel form = new ReceiptFormViewModel();

        Receipt receipt = findReceiptById(context, receiptId);
        form.setTitle(receipt.getAction().getName());
        form.setOpen(receipt.isOpen());

        Category category = new CategoryDAO(context).findById(receipt.getAction().getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        form.setColor(application.getReceiptColor());
        form.setId(receipt.getId());

        List<Field> headers = new FieldDAO(context).findForReceipt(receiptId);

        for (Field header : headers){

            FieldViewModel field = new FieldViewModel();
            field.setTag(header.getColumn().getId());
            field.setLabel(header.getColumn().getName());
            field.setValue(header.getValue());

            if(header.getColumn().isCombo()){

                Column column = header.getColumn();

                List<Column> relationship = new ColumnDAO(context).findLogicColumnsForLayoutAndTable(column.getRelationship().getExternalID(), column.getRelationship().getTable().getId());

                List<FieldViewModel> fields = new ArrayList<>();

                Record record = new RecordDAO(context).findForTableAndValueId(column.getRelationship().getExternalID(), header.getValue());

                if(record != null)
                    record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                for (Column col : relationship){

                    if(record != null) {
                        field.setValue(record.getFieldForColumn(col.getId()).getValue());
                    }
                }

                field.setRelationshipFields(fields);
            }
            form.addField(field);
        }

        return form;
    }

    public ReceiptTableViewModel getReceiptForAction(Context context, String actionId) {

        ReceiptTableViewModel table = new ReceiptTableViewModel();

        Action action = new ActionDAO(context).findById(actionId);
        table.setName(action.getName());

        List<ReceiptCellViewModel> cells = new ArrayList<>();

        List<Receipt> receipts = new ReceiptDAO(context).findForAction(actionId);

        for (int index = 0; index < receipts.size(); index++) {

            Receipt receipt = receipts.get(index);

            ReceiptCellViewModel cell = new ReceiptCellViewModel();
            cell.setId(receipt.getId());
            cell.setOpen(receipt.isOpen());

            List<FieldViewModel> fieldViewModels = new ArrayList<>();

//            FieldViewModel id = new FieldViewModel();
//            id.setValue(String.valueOf(receipt.getId()));
//            id.setLabel("Id");
//            id.setType(FieldType.TEXT);
//
//            fieldViewModels.add(id);
//
//            int itemsCount = new ReceiptItemDAO(context).countItemsForReceipt(receipt.getId());
//
//            FieldViewModel items = new FieldViewModel();
//            items.setValue(String.valueOf(itemsCount));
//            items.setLabel(receipt.getAction().getTable().getName());
//            items.setType(FieldType.TEXT);
//
//            fieldViewModels.add(items);

            for (ReceiptItem item : receipt.getItems()) {
                Collection<Field> fields = item.getRecord().getFields();

                for (Field recordField : fields) {

                    if (recordField.getColumn().getId().equals("OM_MOVILNOVEDAD_C_0038"))
                        cell.setCreated(recordField.getValue().isEmpty());

                    if (recordField.getColumn().getId().equals("OM_MOVILNOVEDAD_CF_0200") ||
                            recordField.getColumn().getId().equals("OM_MOVILNOVEDAD_C_0049") ||
                            recordField.getColumn().getId().equals("OM_MOVILNOVEDAD_C_0106")) {

                        FieldViewModel field = new FieldViewModel();
                        field.setValue(recordField.getValue());
                        field.setLabel(recordField.getColumn().getName());
                        field.setType(recordField.getColumn().getFieldType());
                        field.setTag(String.valueOf(recordField.getColumn().getId()));

                        fieldViewModels.add(field);
                    }
                }
            }

            for (Field headerField : receipt.getHeaderValues()) {

                FieldViewModel field = new FieldViewModel();
                field.setValue(headerField.getValue());
                field.setLabel(headerField.getColumn().getName());
                field.setType(headerField.getColumn().getFieldType());
                field.setTag(String.valueOf(headerField.getColumn().getId()));

                if (headerField.getColumn().getFieldType() == FieldType.COMBO) {

                    field.setRelationshipTable(headerField.getColumn().getRelationship().getExternalID());

                    Column column = headerField.getColumn();

                    List<Column> relationship = new ColumnDAO(context).findLogicColumnsForLayoutAndTable(column.getRelationship().getExternalID(), column.getRelationship().getTable().getId());

                    Record record = new RecordDAO(context).findForTableAndValueId(column.getRelationship().getExternalID(), headerField.getValue());

                    if (record != null)
                        record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                    for (Column col : relationship) {
                        if (record != null)
                            field.setValue(record.getFieldForColumn(col.getId()).getValue());
                    }
                }

                fieldViewModels.add(field);
            }

            cell.setFields(fieldViewModels);
            cells.add(cell);
        }

        table.setReceipts(cells);

        return table;
    }

    public FormViewModel newReceiptHeader(Context context, String actionId){
        FormViewModel form = new FormViewModel();

        Action action = new ActionDAO(context).findById(actionId);

        Category category = new CategoryDAO(context).findById(action.getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        form.setColor(application.getReceiptColor());

        form.setTitle(action.getName());

        ColumnActionDAO columnActionDAO = new ColumnActionDAO(context);

        List<ColumnAction> columnsHeader = columnActionDAO.
                findColumnsForActionAndType(actionId, ColumnAction.ColumnActionType.HEADER);

        for (ColumnAction columnAction : columnsHeader){

            FieldViewModel field = new FieldViewModel();
            field.setLabel(columnAction.getColumn().getName());
            field.setType(columnAction.getColumn().getFieldType());
            field.setValue(columnAction.getDefaultValue());
            field.setTag(String.valueOf(columnAction.getColumn().getId()));
            field.setEditable(columnAction.isEditable());

            if(columnAction.getColumn().isCombo()){

                Layout layout = columnAction.getColumn().getRelationship();

                field.setRelationshipTable(layout.getTable().getId());

                Column column = columnAction.getColumn();

                List<Column> relationship = new ColumnDAO(context).findLogicColumnsForLayoutAndTable(layout.getExternalID(), layout.getTable().getId());

                List<FieldViewModel> fields = new ArrayList<>();

                Record record = new RecordDAO(context).findForTableAndValueId(column.getRelationship().getTable().getId(), columnAction.getDefaultValue());

                if(record != null)
                    record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                for (Column col : relationship){

                    FieldViewModel subField = new FieldViewModel();
                    subField.setLabel(col.getName());
                    subField.setType(col.getFieldType());
                    subField.setTag(String.valueOf(col.getId()));

                    if(record != null) {
                        subField.setValue(record.getFieldForColumn(col.getId()).getValue());
                        field.setEditable(false);
                    }

                    fields.add(subField);
                }

                field.setRelationshipFields(fields);
            }

            form.addField(field);
        }

        return form;
    }

    public FormViewModel getStatus(Context context, Long receiptId){
        FormViewModel form = new FormViewModel();

        Receipt receipt = findReceiptById(context, receiptId);

        Action action = receipt.getAction();

        Category category = new CategoryDAO(context).findById(action.getCategory().getId());

        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        form.setColor(application.getRecordColor());

        List<ColumnAction> columnActions = new ColumnActionDAO(context).findColumnsForActionAndType(action.getId(), ColumnAction.ColumnActionType.HEADER);

        form.setTitle(action.getName());

        List<Field> headers = new FieldDAO(context).findForReceipt(receiptId);

        for (Field header : headers){

            FieldViewModel field = new FieldViewModel();
            field.setTag(header.getColumn().getId());
            field.setLabel(header.getColumn().getName());
            field.setValue(header.getValue());
            field.setType(FieldType.TEXT);

            PairViewModel pair = new PairViewModel();
            pair.setFirstField(field);

            if(header.getColumn().isCombo()){

                Column column = header.getColumn();

                List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getExternalID());

                List<FieldViewModel> fields = new ArrayList<>();

                Record record = new RecordDAO(context).findForTableAndValueId(column.getRelationship().getExternalID(), header.getValue());

                if(record != null)
                    record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                for (Column col : relationship){

                    if(record != null) {
                        field.setValue(record.getFieldForColumn(col.getId()).getValue());
                    }
                }

                field.setRelationshipFields(fields);

                FieldViewModel lastField = loadLastValue(context, header, columnActions);
                if(lastField != null)
                    pair.setSecondField(lastField);
            }
//
            form.addPair(pair);
        }

        return form;
    }

    private FieldViewModel loadLastValue(Context context, Field field, List<ColumnAction> columnActions){

        for (ColumnAction columnAction : columnActions){

            if(columnAction.getColumn().getId().equalsIgnoreCase(field.getColumn().getId()) &&
                    columnAction.getLastValue() != null) {
                FieldViewModel fieldViewModel = new FieldViewModel();
                fieldViewModel.setTag(columnAction.getColumn().getId());
                fieldViewModel.setLabel(columnAction.getColumn().getName());
                fieldViewModel.setValue(columnAction.getLastValue());
                fieldViewModel.setType(FieldType.TEXT);

                if (columnAction.getColumn().isCombo()) {

                    Column column = columnAction.getColumn();

                    List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getExternalID());

                    List<FieldViewModel> fields = new ArrayList<>();

                    Record record = new RecordDAO(context).findForTableAndValueId(column.getRelationship().getExternalID(), columnAction.getLastValue());

                    if (record != null)
                        record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                    for (Column col : relationship) {

                        if (record != null) {
                            fieldViewModel.setValue(record.getFieldForColumn(col.getId()).getValue());
                        }
                    }

                    fieldViewModel.setRelationshipFields(fields);
                }

                return  fieldViewModel;
            }
        }

        return null;
    }

    public TableViewModel getItemsByReceipt(Context context, long receiptId){
        TableViewModel table = new TableViewModel();

        Receipt receipt = findReceiptById(context, receiptId);

        table.setName(receipt.getAction().getTable().getName());

        ReceiptItemDAO dao = new ReceiptItemDAO(context);

        List<ReceiptItem> records = dao.findForReceipt(receiptId);

        for (ReceiptItem item: records) {
            CellViewModel cell = createCell(context, item.getRecord().getId(), receipt);
            table.addCell(cell);
        }

        return table;
    }

    public List<CellViewModel> findReceiptItems(Context context, long receiptId, long[] recordIds){
        List<CellViewModel> cells = new ArrayList<>();

        Receipt receipt = findReceiptById(context, receiptId);

        Action action = receipt.getAction();

        RecordDAO dao = new RecordDAO(context);

        List<Record> records = new ArrayList<>();

        for (long recordId: recordIds) {
            Record record = dao.findById(recordId);
            records.add(record);
        }

        for (Record record: filter(records, action)) {
            CellViewModel cell = createCell(context, record.getId(), receipt);


            cells.add(cell);
        }

        return cells;
    }

    private List<Record> filter(List<Record> records, Action action){

        List<Record> filterRecords = new ArrayList<>();
        filterRecords.addAll(records);

        for (ColumnAction columnAction : action.getColumnsHeader()){
            if(columnAction.getLastValue() != null && !columnAction.getLastValue().isEmpty()){
                List<Record> aux = applySubFilter(filterRecords, columnAction.getColumn().getId(), columnAction.getLastValue());
                filterRecords.clear();
                filterRecords.addAll(aux);
            }
        }

        return filterRecords;
    }

//    public void createCell(CellViewModel cell, List<Field> fields){
//
//        List<FieldViewModel> fieldViewModels = new ArrayList<>();
//
//        for (Field field : fields){
//            FieldViewModel fieldViewModel = new FieldViewModel();
//            fieldViewModel.setValue(field.getValue());
//            fieldViewModel.setTag(field.getColumn().getId());
//            fieldViewModel.setLabel(field.getColumn().getName());
//            fieldViewModel.setPrimaryKey(field.getColumn().isPrimaryKey());
//            fieldViewModel.setHighlight(field.getColumn().isHighlight());
//
//            fieldViewModels.add(fieldViewModel);
//        }
//
//        cell.setFields(fieldViewModels);
//    }

    public List<Record> applySubFilter(List<Record> records, String columnId, String value){

        List<Record> subList = new ArrayList<>();

        for (Record record : records){

            Field field = record.getFieldForColumn(columnId);

            if(field != null && field.getValue().contains(value)){
                subList.add(record);
            }
        }

        return subList;
    }

    private CellViewModel createCell(Context context, long recordId, Receipt receipt) {

        CellViewModel cell = new CellViewModel();
        cell.setId(recordId);

        List<Field> fields = new FieldDAO(context)
                .findVisiblesForRecordAndLayout(String.valueOf(cell.getId()), receipt.getAction().getId());

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

//        HistoryDAO historyDAO = new HistoryDAO(context);

        for (Field field : fields) {

            if (field.getColumn().getFieldType() != FieldType.MAP &&
                    field.getColumn().getFieldType() != FieldType.TIME) {

//                History history = historyDAO.findForReceiptAndField(String.valueOf(receipt.getId()), String.valueOf(field.getId()));
//
//                if (history != null && history.getReceipt().getId() == receipt.getId()) {
//                    field.setValue(history.getValue());
//                }

                FieldViewModel fieldViewModel = new FieldViewModel();
                fieldViewModel.setValue(field.getValue());
                fieldViewModel.setLabel(field.getColumn().getName());
                fieldViewModel.setHighlight(field.getColumn().isHighlight());
                fieldViewModel.setEditable(field.getColumn().isEditable());
                fieldViewModels.add(fieldViewModel);
            }
        }

        cell.setFields(fieldViewModels);

        return cell;
    }

    public Receipt updateReceiptItems(Context context, Long receiptId, long[] recordIds, Location lastLocation, Boolean close){

        ReceiptDAO dao = new ReceiptDAO(context);

        Receipt receipt = dao.findById(receiptId);
        receipt.setConfirmed(true);

        if(close)
            receipt.setClose();
        else
            receipt.setOpen();

        receipt.setCreatedAt(DateTimeHelper.formatDateTime(new Date()));

        Status status = new Status();
        status.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
        status.setTimezone(DateTimeHelper.getDeviceTimezone());
        status.setReceipt(receipt);

        if(lastLocation != null){
            status.setLatitude(lastLocation.getLatitude());
            status.setLongitude(lastLocation.getLongitude());
        }

        new StatusDAO(context).save(status);

        receipt.setStatus(status);

        dao.update(receipt);

        List<Record> records = new ArrayList<>();

        RecordDAO recordDAO = new RecordDAO(context);
        FieldDAO fieldDAO = new FieldDAO(context);
        ReceiptItemDAO itemDAO = new ReceiptItemDAO(context);
        itemDAO.deleteForReceipt(receipt.getId());
        List<ReceiptItem> items = new ArrayList<>();

//        HistoryDAO historyDAO = new HistoryDAO(context);

        for (long id : recordIds){
            Record record = recordDAO.findById(id);

            ReceiptItem item = new ReceiptItem(receipt, record);
            items.add(item);

            itemDAO.save(item);
            records.add(record);

            ReceiptCacheManager.getInstance().fillRecord(record);

            for (Field headerField : receipt.getHeaderValues()) {
                Field field = record.getFieldForColumn(headerField.getColumn().getId());

                if(field != null)
                    field.setValue(headerField.getValue());
            }


//            List<History> histories = new ArrayList<>();
//
//            for (Field field : record.getFields()) {
//
//                History history = historyDAO.findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));
//
//                if(history == null || !history.getValue().equalsIgnoreCase(field.getValue())){
//                    history = new History();
//                    history.setValue(field.getValue());
//                    history.setField(field);
//                    history.setSystemDate(DateTimeHelper.formatDateTime(new Date()));
//                    history.setTimeZone(DateTimeHelper.getDeviceTimezone());
//                    history.setReceipt(receipt);
//
//                    histories.add(history);
//                }
//            }
//
//            historyDAO.save(histories);

            fieldDAO.update(record.getFields());
        }

        receipt.setItems(items);

        recordDAO.save(records);

        return receipt;
    }

    public TableViewModel getAvailableItems(Context context, Long receiptId, String query) {
        TableViewModel tableView = new TableViewModel();

        if (query != null && query.isEmpty()) {
            return tableView;
        }

        Receipt receipt = new ReceiptDAO(context).findById(receiptId);
        Action action = receipt.getAction();

        tableView.setName(action.getTable().getName());

        action.setColumnsHeader(new ColumnActionDAO(context).
                findColumnsForActionAndType(action.getId(), ColumnAction.ColumnActionType.HEADER));

        TableDAO tableDAO = new TableDAO(context);
        String tableId = action.getTable().getId();
        Table table = tableDAO.findById(tableId);
        if(table == null) return null;

        Category category = new CategoryDAO(context).findById(action.getCategory().getId());
        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

        tableView.setColor(application.getRecordColor());

        List<Record> records = new RecordDAO(context).findAvailablesForTableAndReceipt(tableId, receipt.getId());

        records = filter(records, action);

        ReceiptItemDAO dao = new ReceiptItemDAO (context); // tomo los que fueron cargados, para filtrarlos mas adelante
        List<ReceiptItem> items = dao.findAll();
        ArrayList<Long> excludeIds = new ArrayList<>();
        for (ReceiptItem item : items)
            excludeIds.add(item.getRecord().getId());

        for (int index = 0; index < records.size(); index++) { //Record record: records){//filter(records, action)) {
            Record record = records.get(index);

// aca filtro los items q estan NO disponibles
            if (!excludeIds.contains(record.getId())) {
                Log.v("FLOW", "ITEM: " + index);
                CellViewModel cell = createCell(record.getId(), receipt, context);
                tableView.addCell(cell);
            }

            if (!((OcasaApplication) context.getApplicationContext()).availableItemsLoading) return null;
        }

        for (String column : action.getOrder().split(",")) tableView.addOrderColumn(column);
        if (tableView.getOrderBy().get(0).isEmpty()) tableView.getOrderBy().clear();

        return tableView;
    }

    private CellViewModel createCell(long recordId, Receipt receipt, Context context) {

        List<Field> fields = new FieldDAO(context).findForRecordAndLayout(String.valueOf(recordId), receipt.getAction().getId());

        CellViewModel cell = new CellViewModel();
        cell.setId(recordId);

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        for (Field field : fields) {
            if (field.getColumn().getFieldType() != FieldType.MAP &&
                    field.getColumn().getFieldType() != FieldType.TIME &&
                    field.getColumn().isDetail()) {   // saco estos campos de la vista AvailableItems!

                FieldViewModel fieldViewModel = new FieldViewModel();
                fieldViewModel.setValue(field.getValue());
                fieldViewModel.setLabel(field.getColumn().getName());
                fieldViewModel.setHighlight(field.getColumn().isHighlight());
                fieldViewModel.setEditable(field.getColumn().isEditable());
                fieldViewModel.setTag(field.getColumn().getId());
                fieldViewModel.setDetail(field.getColumn().isDetail());
                fieldViewModel.setVisible(field.getColumn().isVisible());
                fieldViewModels.add(fieldViewModel);
            }
        }

        cell.setFields(fieldViewModels);

        return cell;
    }

    public List<Receipt> getOpenReceipts(Context context) {

        ReceiptDAO receiptDAO = new ReceiptDAO(context);

        return receiptDAO.findOpens();
    }
}
