package com.dev.server.controllers;

import com.dev.domain.Customer;
import com.dev.server.services.SalesService;
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

@RestController
public class SalesController {

    @Autowired
    private SalesService service;  //Service which will do all data retrieval/manipulation work


    //-------------------Retrieve All Customers--------------------------------------------------------

    @RequestMapping(value = "/customers", method = GET)
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = service.getCustomers();
        if (customers.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);//You many deccodee to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(customers, OK);
    }


    //-------------------Retrieve Single Customer--------------------------------------------------------

    @RequestMapping(value = "/customers/{code}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomer(@PathVariable("code") String code) {
        System.out.println("Fetching Customer with code " + code);
        Customer user = service.getCustomerByCode(code);
        if (user == null) {
            System.out.println("Customer with code " + code + " not found");
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(user, OK);
    }


    //-------------------Create a Customer--------------------------------------------------------

    @RequestMapping(value = "/customers", method = POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Customer " + customer.getOrganizationName());

        if (service.isCustomerExist(customer)) {
            System.out.println("A Customer with name " + customer.getOrganizationName() + " already exist");
            return new ResponseEntity<>(CONFLICT);
        }

        service.saveCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{code}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<>(headers, CREATED);
    }


    //------------------- Update a Customer --------------------------------------------------------

    @RequestMapping(value = "/customers/{code}", method = PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("code") String code, @RequestBody Customer customer) {
        System.out.println("Updating Customer " + customer);

        Customer currentCustomer = service.getCustomerByCode(code);

        if (currentCustomer == null) {
            System.out.println("Customer with code " + code + " not found");
            return new ResponseEntity<>(NOT_FOUND);
        }

        currentCustomer.setOrganizationName(customer.getOrganizationName());
        currentCustomer.setBalance(customer.getBalance());
        currentCustomer.setSalesOrders(customer.getSalesOrders());

        service.saveCustomer(currentCustomer);
        return new ResponseEntity<>(currentCustomer, OK);
    }

    //------------------- Delete a Customer --------------------------------------------------------

    @RequestMapping(value = "/customers/{code}", method = DELETE)
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("code") String code) {
        System.out.println("Fetching & Deleting Customer with code " + code);

        Customer customer = service.getCustomerByCode(code);
        if (customer == null) {
            System.out.println("Unable to delete. Customer with code " + code + " not found");
            return new ResponseEntity<>(NOT_FOUND);
        }

        service.deleteCustomerByCode(code);
        return new ResponseEntity<>(NO_CONTENT);
    }


    //------------------- Delete All Customers --------------------------------------------------------

    @RequestMapping(value = "/customers/", method = DELETE)
    public ResponseEntity<Customer> deleteAllCustomers() {
        System.out.println("Deleting All Customers");

        service.deleteAllCustomers();
        return new ResponseEntity<>(NO_CONTENT);
    }

}