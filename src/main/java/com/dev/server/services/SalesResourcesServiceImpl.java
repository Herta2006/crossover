package com.dev.server.services;

import com.dev.domain.Customer;
import com.dev.domain.OrderLine;
import com.dev.domain.Product;
import com.dev.domain.SalesOrder;
import com.dev.server.NotEnoughBalanceException;
import com.dev.server.NotEnoughQuantityProductException;
import com.dev.server.repositories.CustomerRepository;
import com.dev.server.repositories.ProductRepository;
import com.dev.server.repositories.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        else if (clazz.getName().equals(Product.class.getName())) return (T) productRepository.findOne(code);
        else return (T) salesOrderRepository.findOne(Long.valueOf(code));
    }

    @Override
    public T save(T entity) {
        if (entity.getClass().getName().equals(Customer.class.getName()))
            return (T) customerRepository.saveAndFlush((Customer) entity);
        else if (entity.getClass().getName().equals(Product.class.getName()))
            return (T) productRepository.saveAndFlush((Product) entity);
        return null;
    }

    @Override
    @Transactional
    public SalesOrder saveOrderLine(SalesOrder salesOrder) throws NotEnoughQuantityProductException, NotEnoughBalanceException {
        Customer customer = null;
        for (Customer c : customerRepository.findAll()) {
            if (c.getId().equals(salesOrder.getCustomerId())) {
                customer = c;
                break;
            }
        }
        Map<String, Product> products = new HashMap<>();
        for (Product p : productRepository.findAll()) {
            products.put(p.getId(), p);
        }

        long totalPrice = 0;
        for (OrderLine orderLine : salesOrder.getOrderLines()) {
            Product product = products.get(orderLine.getProductId());
            if (product.getInventoryBalance() < orderLine.getQuantity()) {
                throw new NotEnoughQuantityProductException("Product with code " + product.getId() +
                        " has inventory balance " + product.getInventoryBalance() +
                        ", but requested " + orderLine.getQuantity());
            }
            totalPrice += orderLine.getQuantity() * product.getPrice();
            product.setInventoryBalance(product.getInventoryBalance() - orderLine.getQuantity());
            productRepository.saveAndFlush(product);
        }
        if (customer != null) {
            if (totalPrice > customer.getBalance())
                throw new NotEnoughBalanceException("Total sales order price is " + totalPrice +
                        " but customer  " + customer + ", has only " + customer.getBalance());
            customer.setBalance(customer.getBalance() - totalPrice);
            customerRepository.saveAndFlush(customer);
        }
        return salesOrderRepository.saveAndFlush(salesOrder);
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
