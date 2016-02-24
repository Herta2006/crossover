package com.dev.server.services;

import com.dev.domain.Customer;
import com.dev.domain.Product;
import com.dev.server.repositories.CustomerRepository;
import com.dev.server.repositories.ProductRepository;
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

    public List<T> findAll(Class<T> clazz) {
        if (clazz.getName().equals(Customer.class.getName())) return (List<T>) customerRepository.findAll();
        else return (List<T>) productRepository.findAll();
    }

    @Override
    public T findOne(Class<T> clazz, String code) {
        if (clazz.getName().equals(Customer.class.getName())) return (T) customerRepository.findOne(code);
        else return (T) productRepository.findOne(code);
    }

    @Override
    public T save(T entity) {
        if (entity.getClass().getName().equals(Customer.class.getName()))
            return (T) customerRepository.saveAndFlush((Customer) entity);
        else return (T) productRepository.saveAndFlush((Product) entity);
    }


    @Override
    public void delete(Class<T> clazz, String code) {
        if (clazz.getName().equals(Customer.class.getName())) customerRepository.delete(code);
        else productRepository.delete(code);
    }

    @Override
    public void deleteAll(Class<T> clazz) {
        if (clazz.getName().equals(Customer.class.getName())) customerRepository.deleteAll();
        else productRepository.deleteAll();
    }
}
