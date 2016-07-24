package com.android.ocasa.dao;

import android.content.Context;

import com.android.ocasa.database.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by ignacio on 10/06/2015.
 */
public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    protected Context context;

    protected Dao<T, ID> dao;

    public GenericDAOImpl(Class<T> entityClass, Context context) {

        this.context = context;

        OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        try {
            dao = helper.getDao(entityClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<T, ID> getDao() {
        return dao;
    }

    @Override
    public synchronized void save(T entity) {
        try {
            dao.createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void save(final Collection<T> entities){

        try {
            dao.callBatchTasks(new Callable<Void>() {
                public Void call() throws SQLException {

                    List<T> items = new ArrayList<T>(entities);

                    for (int index = 0; index < entities.size(); index++) {
                        T entity = items.get(index);
                        dao.createOrUpdate(entity);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(T entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(final Collection<T> entities) {
        try {
            dao.callBatchTasks(new Callable<Void>() {
                public Void call() throws SQLException {
                    for (T entity : entities) {
                        dao.update(entity);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh(T entity) {
        try {
            dao.refresh(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ID id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        try {
            dao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T findById(ID id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}