package com.dev.server.services;

import com.dev.domain.Customer;

import java.util.List;

public interface SalesService {
    List<Customer> getCustomers();

    Customer getCustomerByCode(String customersCode);

    void saveCustomer(Customer customer);

    void deleteCustomerByCode(String code);

    boolean isCustomerExist(Customer customer);

    void deleteAllCustomers();
}
