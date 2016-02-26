package com.dev.server.services;

import com.dev.domain.Customer;
import com.dev.domain.SalesOrder;
import com.dev.server.NotEnoughBalanceException;
import com.dev.server.NotEnoughQuantityProductException;

import java.util.List;

public interface SalesResourcesService<T> {
    List<T> findAll(Class<T> clazz);

    T save(T entity);

    T findOne(Class<T> clazz, String code);

    SalesOrder saveOrderLine(SalesOrder salesOrder) throws NotEnoughQuantityProductException, NotEnoughBalanceException;

    void delete(Class<T> clazz, String code);

    void deleteAll(Class<T> clazz);
}
