package com.android.ocasa.service;

import android.content.Context;

import com.android.ocasa.api.OcasaApi;
import com.android.ocasa.core.TableView;
import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ApplicationDAO;
import com.android.ocasa.dao.CategoryDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.ReceiptDAO;
import com.android.ocasa.dao.ReceiptItemDAO;
import com.android.ocasa.dao.RecordDAO;
import com.android.ocasa.dao.TableDAO;
import com.android.ocasa.httpmodel.HttpTable;
import com.android.ocasa.httpmodel.Menu;
import com.android.ocasa.httpmodel.TableRecord;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Table;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;
import com.android.ocasa.viewmodel.FormViewModel;
import com.android.ocasa.viewmodel.PairViewModel;
import com.android.ocasa.viewmodel.ReceiptCellViewModel;
import com.android.ocasa.viewmodel.ReceiptFormViewModel;
import com.android.ocasa.viewmodel.ReceiptTableViewModel;
import com.android.ocasa.viewmodel.TableViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by ignacio on 07/07/16.
 */
public class OcasaService {

    private static OcasaService instance;

    private Context context;

    private OcasaApi api;

    public void init(Context context, OcasaApi api){
        this.context = context;
        this.api = api;
    }

    public static OcasaService getInstance() {
        if(instance == null)
            instance = new OcasaService();
        return instance;
    }

    public Observable<HttpTable> sync(){
        return menuWithCache()
                .flatMap(new Func1<Menu, Observable<Application>>() {
                    @Override
                    public Observable<Application> call(Menu menu) {
                        return Observable.from(menu.getApplications());
                    }
                })
                .flatMap(new Func1<Application, Observable<Category>>() {
                    @Override
                    public Observable<Category> call(Application application) {
                        return Observable.from(application.getCategories());
                    }
                })
                .flatMap(new Func1<Category, Observable<Table>>() {
                    @Override
                    public Observable<Table> call(Category category) {
                        return Observable.from(category.getTables());
                    }
                })
                .flatMap(new Func1<Table, Observable<HttpTable>>() {
                    @Override
                    public Observable<HttpTable> call(Table table) {
                        return tableWithCache(table);
                    }
                }).flatMap(new Func1<HttpTable, Observable<Column>>() {
                    @Override
                    public Observable<Column> call(HttpTable httpTable) {
                        return Observable.from(httpTable.getTable().getColumns());
                    }
                }).filter(new Func1<Column, Boolean>() {
                    @Override
                    public Boolean call(Column column) {
                        return column.getFieldType() == FieldType.COMBO;
                    }
                }).flatMap(new Func1<Column, Observable<HttpTable>>() {
                    @Override
                    public Observable<HttpTable> call(Column column) {
                        Table table = column.getRelationship();
                        return tableWithCache(table);
                    }
                }).last();
    }

    private Observable<Menu> menuWithCache(){
        return api.menu().doOnNext(new Action1<Menu>() {
            @Override
            public void call(Menu menu) {
                new MenuService().save(context, menu);
            }
        });
    }

    public Observable<List<Application>> menu(){
        return Observable.create(new Observable.OnSubscribe<List<Application>>() {
            @Override
            public void call(Subscriber<? super List<Application>> subscriber) {
                subscriber.onNext(new ApplicationDAO(context).findAll());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<HttpTable> tableWithCache(Table table){
        return Observable.zip(
                api.columns(table.getId(), "column" + table.getId() + ".json"),
                api.records(table.getId(), "record" + table.getId() + ".json"),
                new Func2<HttpTable, TableRecord, HttpTable>() {
                    @Override
                    public HttpTable call(HttpTable httpTable, TableRecord tableRecord) {
                        httpTable.getTable().setRecords(tableRecord.getRecords());
                        return httpTable;
                    }
                })
                .doOnNext(new Action1<HttpTable>() {
                    @Override
                    public void call(HttpTable httpTable) {
                        new TableService().saveTable(context, httpTable);
                    }
                });
    }

    public Observable<ReceiptFormViewModel> receiptHeader(long receiptId){
        return Observable.just(receiptId).map(new Func1<Long, ReceiptFormViewModel>() {
            @Override
            public ReceiptFormViewModel call(Long receiptId) {
                return new ReceiptService().getHeaderByReceipt(context, receiptId);
            }
        });
    }

    public Observable<TableViewModel> receiptItems(long receiptId){
        return Observable.just(receiptId).map(new Func1<Long, TableViewModel>() {
            @Override
            public TableViewModel call(Long receiptId) {
                return new ReceiptService().getItemsByReceipt(context, receiptId);
            }
        });
    }

    public Observable<List<CellViewModel>> findReceiptItems(long receiptId, long[] recordIds){
        return Observable.zip(Observable.just(receiptId), Observable.just(recordIds), new Func2<Long, long[], List<CellViewModel>>() {
            @Override
            public List<CellViewModel> call(Long receiptId, long[] recordIds) {

                List<CellViewModel> cells = new ArrayList<>();

                Receipt receipt = new ReceiptDAO(context).findById(receiptId);

                Action action = receipt.getAction();

                RecordDAO dao = RecordDAO.getInstance(context);

                List<Record> records = new ArrayList<>();

                for (long recordId: recordIds) {
                    Record record = dao.findById(recordId);

                    records.add(record);
                }

                for (Record record: filter(records, action)) {
                    CellViewModel cell = new CellViewModel();
                    cell.setId(record.getId());

                    fillCell(cell, record.getLogicFields());
                    cells.add(cell);
                }

                return cells;
            }
        });
    }

    private List<Record> filter(List<Record> records, Action action){

        List<Record> filterRecords = new ArrayList<>();
        filterRecords.addAll(records);

        for (ColumnAction columnAction : action.getColumnsHeader()){
            if(columnAction.getLastValue() != null){
                List<Record> aux = applySubFilter(filterRecords, columnAction.getColumn().getId(), columnAction.getLastValue());
                filterRecords.clear();
                filterRecords.addAll(aux);
            }
        }

        return filterRecords;
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

    public Observable<ReceiptTableViewModel> receiptsByAction(String actionId){
        return Observable.just(actionId).map(new Func1<String, ReceiptTableViewModel>() {
            @Override
            public ReceiptTableViewModel call(String actionId) {

                ReceiptTableViewModel table = new ReceiptTableViewModel();

                Action action = new ActionDAO(context).findById(actionId);
                table.setName(action.getName());

                List<ReceiptCellViewModel> cells = new ArrayList<>();

                List<Receipt> receipts = new ReceiptDAO(context).findForAction(actionId);

                for (Receipt receipt : receipts) {

                    ReceiptCellViewModel cell = new ReceiptCellViewModel();
                    cell.setId(receipt.getId());
                    cell.setOpen(receipt.isOpen());

                    List<FieldViewModel> fieldViewModels = new ArrayList<>();

                    FieldViewModel id = new FieldViewModel();
                    id.setValue(String.valueOf(receipt.getId()));
                    id.setLabel("Id");
                    id.setType(FieldType.TEXT);

                    fieldViewModels.add(id);

                    int itemsCount = new ReceiptItemDAO(context).countItemsForReceipt(receipt.getId());

                    FieldViewModel items = new FieldViewModel();
                    items.setValue(String.valueOf(itemsCount));
                    items.setLabel(receipt.getAction().getTable().getName());
                    items.setType(FieldType.TEXT);

                    fieldViewModels.add(items);

                    for (Field headerField : receipt.getHeaderValues()) {

                        FieldViewModel field = new FieldViewModel();
                        field.setValue(headerField.getValue());
                        field.setLabel(headerField.getColumn().getName());
                        field.setType(headerField.getColumn().getFieldType());
                        field.setTag(String.valueOf(headerField.getColumn().getId()));

                        if (headerField.getColumn().getFieldType() == FieldType.COMBO) {

                            field.setRelationshipTable(headerField.getColumn().getRelationship().getId());

                            Column column = headerField.getColumn();

                            List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getId());

                            Record record = RecordDAO.getInstance(context).findForTableAndValueId(column.getRelationship().getId(), headerField.getValue());

                            if (record != null)
                                record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                            for (Column col : relationship) {

//                        FieldViewModel subField = new FieldViewModel();
//                        subField.setLabel(col.getName());
//                        subField.setType(col.getFieldType());
//                        subField.setTag(String.valueOf(col.getId()));

                                if (record != null)
                                    field.setValue(record.getFieldForColumn(col.getId()).getValue());

//                        fields.add(subField);
                            }

//                    field.setRelationshipFields(fields);

                        }

                        fieldViewModels.add(field);
                    }

                    cell.setFields(fieldViewModels);
                    cells.add(cell);
                }

                table.setReceipts(cells);

                return table;
            }
        });
    }

    public Observable<FormViewModel> record(long recordId){
        return Observable.just(recordId).map(new Func1<Long, FormViewModel>() {
            @Override
            public FormViewModel call(Long recordId) {
                RecordService service = new RecordService(context);

                return service.getFormFromRecord(recordId);
            }
        });
    }

    public Observable<FormViewModel> recordForm(String tableId){
        return Observable.just(tableId).map(new Func1<String, FormViewModel>() {
            @Override
            public FormViewModel call(String tableId) {

                RecordService service = new RecordService(context);
                return service.getFormFromTable(tableId);
            }
        });
    }

    public Observable<FormViewModel> headerreceipt(String actionId){
        return Observable.just(actionId).map(new Func1<String, FormViewModel>() {
            @Override
            public FormViewModel call(String actionId) {
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

                    if(columnAction.getColumn().getFieldType() == FieldType.COMBO){

                        field.setRelationshipTable(columnAction.getColumn().getRelationship().getId());

                        Column column = columnAction.getColumn();

                        List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getId());

                        List<FieldViewModel> fields = new ArrayList<>();

                        Record record = RecordDAO.getInstance(context).findForTableAndValueId(column.getRelationship().getId(), columnAction.getDefaultValue());

                        if(record != null)
                            record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                        for (Column col : relationship){

                            FieldViewModel subField = new FieldViewModel();
                            subField.setLabel(col.getName());
                            subField.setType(col.getFieldType());
                            subField.setTag(String.valueOf(col.getId()));

                            if(record != null)
                                subField.setValue(record.getFieldForColumn(col.getId()).getValue());

                            fields.add(subField);
                        }

                        field.setRelationshipFields(fields);
                    }

                    form.addField(field);
                }

                return form;
            }
        });
    }

    public Observable<FormViewModel> receiptItem(long recordId, long receiptId){
        return Observable.zip(
                Observable.just(recordId),
                Observable.just(receiptId),
                new Func2<Long, Long, FormViewModel>() {
                    @Override
                    public FormViewModel call(Long recordId, Long receiptId) {
                        RecordService service = new RecordService(context);

                        return service.getFormFromRecordAndReceipt(recordId, receiptId);
                    }
                });
    }

    public Observable<FormViewModel> updateReceiptItem(long recordId, long receiptId){
        return Observable.zip(
                Observable.just(recordId),
                Observable.just(receiptId),
                new Func2<Long, Long, FormViewModel>() {
                    @Override
                    public FormViewModel call(Long recordId, Long receiptId) {
                        RecordService service = new RecordService(context);

                        return service.getFromFromRecordAndReceipt(recordId, receiptId);
                    }
                });
    }

    public Observable<FormViewModel> receiptStatus(long receiptId){
        return Observable.just(receiptId).map(new Func1<Long, FormViewModel>() {
            @Override
            public FormViewModel call(Long receiptId) {
                FormViewModel form = new FormViewModel();

                Receipt receipt = new ReceiptDAO(context).findById(receiptId);

                Action action = receipt.getAction();

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

                    if(header.getColumn().getFieldType() == FieldType.COMBO){

                        Column column = header.getColumn();

                        List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getId());

                        List<FieldViewModel> fields = new ArrayList<>();

                        Record record = RecordDAO.getInstance(context).findForTableAndValueId(column.getRelationship().getId(), header.getValue());

                        if(record != null)
                            record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                        for (Column col : relationship){

//                    FieldViewModel subField = new FieldViewModel();
//                    subField.setLabel(col.getName());
//                    subField.setType(col.getFieldType());
//                    subField.setTag(String.valueOf(col.getId()));

                            if(record != null) {
                                //subField.setValue(record.getFieldForColumn(col.getId()).getValue());
                                field.setValue(record.getFieldForColumn(col.getId()).getValue());
                            }

//                    fields.add(subField);
                        }

                        field.setRelationshipFields(fields);

                        FieldViewModel lastField = loadLastValue(header, columnActions);
                        if(lastField != null)
                            pair.setSecondField(lastField);
                    }
//
                    form.addPair(pair);
                }

                return form;
            }
        });
    }

    private FieldViewModel loadLastValue(Field field, List<ColumnAction> columnActions){

        for (ColumnAction columnAction : columnActions){

            if(columnAction.getColumn().getId().equalsIgnoreCase(field.getColumn().getId()) &&
                    columnAction.getLastValue() != null) {
                FieldViewModel fieldViewModel = new FieldViewModel();
                fieldViewModel.setTag(columnAction.getColumn().getId());
                fieldViewModel.setLabel(columnAction.getColumn().getName());
                fieldViewModel.setValue(columnAction.getLastValue());
                fieldViewModel.setType(FieldType.TEXT);

                if (columnAction.getColumn().getFieldType() == FieldType.COMBO) {

                    Column column = columnAction.getColumn();

                    List<Column> relationship = new ColumnDAO(context).findLogicColumnsForTable(column.getRelationship().getId());

                    List<FieldViewModel> fields = new ArrayList<>();

                    Record record = RecordDAO.getInstance(context).findForTableAndValueId(column.getRelationship().getId(), columnAction.getLastValue());

                    if (record != null)
                        record.setFields(new FieldDAO(context).findLogicsForRecord(String.valueOf(record.getId())));

                    for (Column col : relationship) {

//                    FieldViewModel subField = new FieldViewModel();
//                    subField.setLabel(col.getName());
//                    subField.setType(col.getFieldType());
//                    subField.setTag(String.valueOf(col.getId()));

                        if (record != null) {
                            //subField.setValue(record.getFieldForColumn(col.getId()).getValue());
                            fieldViewModel.setValue(record.getFieldForColumn(col.getId()).getValue());
                        }

//                    fields.add(subField);
                    }

                    fieldViewModel.setRelationshipFields(fields);
                }

                return  fieldViewModel;
            }
        }

        return null;
    }

    public Observable<TableViewModel> table(String tableId, String query, long[] excludeIds){

        return Observable.zip(Observable.just(tableId),
                Observable.just(query),
                Observable.just(excludeIds), new Func3<String, String, long[], TableViewModel>() {
                    @Override
                    public TableViewModel call(String tableId, String query, long[] excludeIds) {
                        TableViewModel tableViewModel = new TableViewModel();

                        Table table = new TableDAO(context).findById(tableId);

                        if(table == null)
                            return null;

                        Category category = new CategoryDAO(context).findById(table.getCategory().getId());

                        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

                        tableViewModel.setColor(application.getRecordColor());
                        tableViewModel.setName(table.getName());

                        List<Record> records = RecordDAO.getInstance(context).findForTableAndQuery(tableId, query, excludeIds);

                        for (int index = 0; index < records.size(); index++){

                            Record record = records.get(index);

                            CellViewModel cell = new CellViewModel();
                            cell.setId(record.getId());
                            cell.setNew(record.isNew());
                            cell.setUpdated(record.isUpdated());
                            cell.setValue(record.getPrimaryKey().getValue());

                            fillCell(cell, record.getLogicFields());
                            tableViewModel.addCell(cell);
                        }

                        return tableViewModel;
                    }
                });
    }

    public Observable<TableViewModel> receiptAvailableItems(long receiptId, String query, long[] excludeIds){
        return Observable.zip(
                Observable.just(receiptId),
                Observable.just(query),
                Observable.just(excludeIds), new Func3<Long, String, long[], TableViewModel>() {
                    @Override
                    public TableViewModel call(Long receiptId, String query, long[] excludeIds) {
                        TableViewModel tableView = new TableViewModel();

                        if(query != null && query.isEmpty()){
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

                        if(table == null)
                            return null;

                        Category category = new CategoryDAO(context).findById(table.getCategory().getId());

                        Application application = new ApplicationDAO(context).findById(category.getApplication().getId());

                        tableView.setColor(application.getRecordColor());

                        List<Record> records = RecordDAO.getInstance(context).findForTableAndQuery(tableId, query, excludeIds);

                        records = filter(records, action);

                        for (int index = 0; index < records.size(); index++){ //Record record: records){//filter(records, action)) {
                            Record record = records.get(index);

                            CellViewModel cell = new CellViewModel();
                            cell.setId(record.getId());

                            fillCell(cell, record.getLogicFields());
                            tableView.addCell(cell);
                        }

                        return tableView;
                    }
                });
    }
}
