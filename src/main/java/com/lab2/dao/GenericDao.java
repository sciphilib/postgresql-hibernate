package com.lab2.dao;

import java.util.List;

public interface GenericDao<T> {
    T findById(Integer id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    List<T> findWithCriteria(String field, String value);
}