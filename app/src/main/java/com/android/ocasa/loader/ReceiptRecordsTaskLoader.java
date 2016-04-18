package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.dao.FieldDAO;
import com.android.ocasa.dao.HistoryDAO;
import com.android.ocasa.dao.ReceiptItemDAO;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.FieldType;
import com.android.ocasa.model.History;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.viewmodel.CellViewModel;
import com.android.ocasa.viewmodel.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emiliano Mallo on 07/04/16.
 */
public class ReceiptRecordsTaskLoader extends AsyncTaskLoader<List<CellViewModel>> {

    private List<CellViewModel> data;
    private long receiptId;

    public ReceiptRecordsTaskLoader(Context context, long receiptId) {
        super(context);
        this.receiptId = receiptId;
    }

    @Override
    public List<CellViewModel> loadInBackground() {

        List<CellViewModel> cells = new ArrayList<>();

        ReceiptItemDAO dao = new ReceiptItemDAO(getContext());

        List<ReceiptItem> records = dao.findForReceipt(receiptId);

        for (ReceiptItem item: records) {
            Record record = item.getRecord();

            CellViewModel cell = new CellViewModel();
            cell.setId(record.getId());

            fillCell(cell);
            cells.add(cell);
        }

        return cells;
    }

    private void fillCell(CellViewModel cell){

        List<Field> fields = new FieldDAO(getContext()).findLogicsForRecord(String.valueOf(cell.getId()));

        List<FieldViewModel> fieldViewModels = new ArrayList<>();

        HistoryDAO historyDAO = new HistoryDAO(getContext());

        for (Field field : fields){
            History history = historyDAO.findForReceiptAndField(String.valueOf(receiptId), String.valueOf(field.getId()));

            if(history != null && history.getReceipt().getId() == receiptId){
                field.setValue(history.getValue());
            }

            FieldViewModel fieldViewModel = new FieldViewModel();
            fieldViewModel.setValue(field.getValue());
            fieldViewModel.setLabel(field.getColumn().getName());

            fieldViewModels.add(fieldViewModel);
        }

        cell.setFields(fieldViewModels);
    }

    @Override
    public void deliverResult(List<CellViewModel> data) {
        this.data = data;

        if(isStarted())
            super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(data != null)
            deliverResult(data);
        else{
            forceLoad();
        }
    }
}
