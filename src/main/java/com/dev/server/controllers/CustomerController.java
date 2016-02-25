package com.dev.server.controllers;

import com.dev.domain.Customer;
import com.dev.server.services.SalesResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping(value = "/customers", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class CustomerController {

    @Autowired
    private SalesResourcesService salesResourcesService;

    @RequestMapping(method = GET)
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = salesResourcesService.findAll(Customer.class);
        if (customers.isEmpty()) return new ResponseEntity<>(NO_CONTENT);
        return new ResponseEntity<>(customers, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        if (salesResourcesService.findOne(Customer.class, customer.getId()) != null)
            return new ResponseEntity<>(CONFLICT);
        salesResourcesService.save(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/customers/{code}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<>(headers, CREATED);
    }

    @RequestMapping(value = "/{code}", method = GET)
    public ResponseEntity<Customer> getCustomer(@PathVariable("code") String code) {
        Customer customer = (Customer) salesResourcesService.findOne(Customer.class, code);
        if (customer == null) return new ResponseEntity<>(NOT_FOUND);
        return new ResponseEntity<>(customer, OK);
    }

    @RequestMapping(value = "/{code}", method = PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("code") String code, @RequestBody Customer customer) {
        Customer currentCustomer = (Customer) salesResourcesService.findOne(Customer.class, code);
        if (currentCustomer == null) return new ResponseEntity<>(NOT_FOUND);
        currentCustomer.setOrganizationName(customer.getOrganizationName());
        currentCustomer.setPhone1(customer.getPhone1());
        currentCustomer.setPhone2(customer.getPhone2());
        currentCustomer.setBalance(customer.getBalance());
        Customer savedCustomer = (Customer) salesResourcesService.save(currentCustomer);
        return new ResponseEntity<>(savedCustomer, ACCEPTED);
    }

    @RequestMapping(value = "/{code}", method = DELETE)
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("code") String code) {
        Customer customer = (Customer) salesResourcesService.findOne(Customer.class, code);
        if (customer == null) return new ResponseEntity<>(NOT_FOUND);
        salesResourcesService.delete(Customer.class, code);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @RequestMapping(method = DELETE)
    public ResponseEntity<Customer> deleteAllCustomers() {
        salesResourcesService.deleteAll(Customer.class);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
