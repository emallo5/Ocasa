package com.android.ocasa.dao;

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
    public void delete(T entity) throws SQLException;
    public void deleteAll();
    public T finById(ID id);
    public List<T> findAll();

}
