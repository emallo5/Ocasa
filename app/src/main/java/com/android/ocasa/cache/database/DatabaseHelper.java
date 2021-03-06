package com.android.ocasa.cache.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.ocasa.model.Action;
import com.android.ocasa.model.Application;
import com.android.ocasa.model.Category;
import com.android.ocasa.model.Column;
import com.android.ocasa.model.ColumnAction;
import com.android.ocasa.model.Field;
import com.android.ocasa.model.History;
import com.android.ocasa.model.Layout;
import com.android.ocasa.model.LayoutColumn;
import com.android.ocasa.model.NewRecordRead;
import com.android.ocasa.model.Receipt;
import com.android.ocasa.model.ReceiptItem;
import com.android.ocasa.model.Record;
import com.android.ocasa.model.Status;
import com.android.ocasa.model.Table;
import com.android.ocasa.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by ignacio on 11/01/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    static final String DB_NAME = "Ocasa.db";
    static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Application.class);
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Table.class);
            TableUtils.createTable(connectionSource, Action.class);
            TableUtils.createTable(connectionSource, ColumnAction.class);
            TableUtils.createTable(connectionSource, Record.class);
            TableUtils.createTable(connectionSource, Field.class);
            TableUtils.createTable(connectionSource, Layout.class);
            TableUtils.createTable(connectionSource, LayoutColumn.class);
            TableUtils.createTable(connectionSource, Column.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, History.class);
            TableUtils.createTable(connectionSource, Receipt.class);
            TableUtils.createTable(connectionSource, Status.class);
            TableUtils.createTable(connectionSource, ReceiptItem.class);
            TableUtils.createTable(connectionSource, NewRecordRead.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, NewRecordRead.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
