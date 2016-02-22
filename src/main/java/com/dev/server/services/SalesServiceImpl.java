package com.dev.server.services;

import com.dev.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saleService")
public class SalesServiceImpl implements SalesService {
    @Override
    public List<Customer> getCustomers() {
        return null;
    }

    @Override
    public Customer getCustomerByCode(String customersCode) {
        return null;
    }

    @Override
    public void saveCustomer(Customer customer) {

    }

    @Override
    public void deleteCustomerByCode(String code) {

    }

    @Override
    public boolean isCustomerExist(Customer customer) {
        return false;
    }

    @Override
    public void deleteAllCustomers() {

    }
}
