package com.android.ocasa.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.ocasa.dao.ActionDAO;
import com.android.ocasa.dao.ColumnActionDAO;
import com.android.ocasa.dao.ColumnDAO;
import com.android.ocasa.model.Action;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.FieldType;

/**
 * Created by Emiliano Mallo on 17/03/16.
 */
public class ActionTaskLoader extends AsyncTaskLoader<Action> {

    private Action data;

    private String actionId;

    public ActionTaskLoader(Context context, String actionId) {
        super(context);
        this.actionId = actionId;
    }

    @Override
    public Action loadInBackground() {

        Action action = new ActionDAO(getContext()).findById(actionId);

        ColumnActionDAO columnActionDAO = new ColumnActionDAO(getContext());

        action.setColumnsHeader(columnActionDAO.
                findColumnsForActionAndType(actionId, ColumnAction.ColumnActionType.HEADER));

        action.setColumnsDetail(columnActionDAO.
                findColumnsForActionAndType(actionId, ColumnAction.ColumnActionType.DETAIL));

        /*for (ColumnAction columnAction : action.getColumnsHeader()){
            if(columnAction.getColumn().getFieldType() == FieldType.COMBO){

                Column column = columnAction.getColumn();

            }
        }*/

        return action;
    }

    @Override
    public void deliverResult(Action data) {
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
