package com.dev.server.services;

import com.dev.domain.Customer;

import java.util.List;

public interface SalesResourcesService<T> {
    List<T> findAll(Class<T> clazz);

    T save(T entity);

    T findOne(Class<T> clazz, String code);

    void delete(Class<T> clazz, String code);

    void deleteAll(Class<T> clazz);
}
