package com.dev.server.services;

import com.dev.domain.Customer;
import com.dev.domain.Product;
import com.dev.domain.SalesOrder;
import com.dev.server.repositories.CustomerRepository;
import com.dev.server.repositories.ProductRepository;
import com.dev.server.repositories.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings(value = "unchecked")
public class SalesResourcesServiceImpl<T> implements SalesResourcesService<T> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    public List<T> findAll(Class<T> clazz) {
        if (clazz.getName().equals(Customer.class.getName())) return (List<T>) customerRepository.findAll();
        else if (clazz.getName().equals(Product.class.getName())) return (List<T>) productRepository.findAll();
        else return (List<T>) salesOrderRepository.findAll();
    }

    @Override
    public T findOne(Class<T> clazz, String code) {
        if (clazz.getName().equals(Customer.class.getName())) return (T) customerRepository.findOne(code);
        else if (clazz.getName().equals(Customer.class.getName())) return (T) productRepository.findOne(code);
        else return (T) salesOrderRepository.findOne(Long.valueOf(code));
    }

    @Override
    public T save(T entity) {
        if (entity.getClass().getName().equals(Customer.class.getName()))
            return (T) customerRepository.saveAndFlush((Customer) entity);
        if (entity.getClass().getName().equals(Customer.class.getName()))
            return (T) productRepository.saveAndFlush((Product) entity);
        else return (T) salesOrderRepository.saveAndFlush((SalesOrder) entity);
    }


    @Override
    public void delete(Class<T> clazz, String code) {
        if (clazz.getName().equals(Customer.class.getName())) customerRepository.delete(code);
        else if (clazz.getName().equals(Product.class.getName())) productRepository.delete(code);
        else salesOrderRepository.delete(Long.valueOf(code));
    }

    @Override
    public void deleteAll(Class<T> clazz) {
        if (clazz.getName().equals(Customer.class.getName())) customerRepository.deleteAll();
        else if (clazz.getName().equals(Customer.class.getName())) productRepository.deleteAll();
        else salesOrderRepository.deleteAll();
    }
}
