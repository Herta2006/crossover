package com.dev.server.services;

import com.dev.domain.Customer;
import com.dev.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saleService")
public class SalesServiceImpl implements SalesService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerByCode(String code) {
        return customerRepository.findOne(code);
    }

    @Override
    public void saveCustomer(Customer customer) {
        Customer dbCustomer = customerRepository.findOne(customer.getId());
        dbCustomer.setBalance(customer.getBalance());
        dbCustomer.setOrganizationName(customer.getOrganizationName());
        dbCustomer.setSalesOrders(customer.getSalesOrders());
    }

    @Override
    public void deleteCustomerByCode(String code) {
        customerRepository.delete(code);
    }

    @Override
    public boolean isCustomerExist(Customer customer) {
        return false;
    }

    @Override
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }
}
