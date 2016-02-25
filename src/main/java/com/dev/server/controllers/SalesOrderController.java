package com.dev.server.controllers;

import com.dev.domain.SalesOrder;
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
@RequestMapping(value = "/salesOrders", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class SalesOrderController {

    @Autowired
    private SalesResourcesService salesResourcesService;

    @RequestMapping(method = GET)
    public ResponseEntity<List<SalesOrder>> getSalesOrders() {
        List<SalesOrder> salesOrders = salesResourcesService.findAll(SalesOrder.class);
        if (salesOrders.isEmpty()) return new ResponseEntity<>(NO_CONTENT);
        return new ResponseEntity<>(salesOrders, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Void> createSalesOrder(@RequestBody SalesOrder salesOrder, UriComponentsBuilder ucBuilder) {
        if (salesResourcesService.findOne(SalesOrder.class, "" + salesOrder.getId()) != null)
            return new ResponseEntity<>(CONFLICT);
        salesResourcesService.save(salesOrder);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/salesOrders/{code}").buildAndExpand(salesOrder.getId()).toUri());
        return new ResponseEntity<>(headers, CREATED);
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<SalesOrder> getSalesOrder(@PathVariable("id") String code) {
        SalesOrder salesOrder = (SalesOrder) salesResourcesService.findOne(SalesOrder.class, code);
        if (salesOrder == null) return new ResponseEntity<>(NOT_FOUND);
        return new ResponseEntity<>(salesOrder, OK);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    public ResponseEntity<SalesOrder> updateSalesOrder(@PathVariable("id") String id, @RequestBody SalesOrder salesOrder) {
        SalesOrder currentSalesOrder = (SalesOrder) salesResourcesService.findOne(SalesOrder.class, id);
        if (currentSalesOrder == null) return new ResponseEntity<>(NOT_FOUND);
        currentSalesOrder.setCustomerId(salesOrder.getCustomerId());
        currentSalesOrder.setOrderLines(salesOrder.getOrderLines());
        SalesOrder savedSalesOrder = (SalesOrder) salesResourcesService.save(currentSalesOrder);
        return new ResponseEntity<>(savedSalesOrder, ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity<SalesOrder> deleteSalesOrder(@PathVariable("id") String id) {
        SalesOrder salesOrder = (SalesOrder) salesResourcesService.findOne(SalesOrder.class, id);
        if (salesOrder == null) return new ResponseEntity<>(NOT_FOUND);
        salesResourcesService.delete(SalesOrder.class, id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @RequestMapping(method = DELETE)
    public ResponseEntity<SalesOrder> deleteAllSalesOrders() {
        salesResourcesService.deleteAll(SalesOrder.class);
        return new ResponseEntity<>(NO_CONTENT);
    }
}

