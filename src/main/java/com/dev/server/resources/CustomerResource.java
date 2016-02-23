package com.dev.server.resources;

import com.dev.domain.Customer;
import com.dev.server.services.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/customers")
@Produces(APPLICATION_JSON)
@Component
public class CustomerResource {

    @Autowired
    SalesService service;

    @GET
    public List<Customer> getCustomers() {
        return service.getCustomers();
    }

    @GET
    @Path("/code")
    public Customer getCustomer(@PathParam(value = "code") String code) {
        return service.getCustomerByCode(code);
    }
}
