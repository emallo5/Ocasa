package com.android.ocasa.cache.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created by ignacio on 10/06/2015.
 */
public interface GenericDAO<T , ID> {

    public void save(T entity);
    public void save(Collection<T> entities) throws Exception;
    public void update(T entity);
    public void update(Collection<T> entity);
    public void refresh(T entity);
    public void delete(ID id) throws SQLException;
    public void deleteAll();
    public T findById(ID id);
    public List<T> findAll();

}
