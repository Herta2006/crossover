package com.dev.server.controllers;

import com.dev.domain.Product;
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
@RequestMapping(value = "/products", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private SalesResourcesService salesResourcesService;

    @RequestMapping(method = GET)
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = salesResourcesService.findAll(Product.class);
        if (products.isEmpty()) return new ResponseEntity<>(NO_CONTENT);
        return new ResponseEntity<>(products, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Void> createUser(@RequestBody Product product, UriComponentsBuilder ucBuilder) {
        if (salesResourcesService.findOne(Product.class, product.getId()) != null)
            return new ResponseEntity<>(CONFLICT);
        salesResourcesService.save(product);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/products/{code}").buildAndExpand(product.getId()).toUri());
        return new ResponseEntity<>(headers, CREATED);
    }

    @RequestMapping(value = "/{code}", method = GET)
    public ResponseEntity<Product> getProduct(@PathVariable("code") String code) {
        Product product = (Product) salesResourcesService.findOne(Product.class, code);
        if (product == null) return new ResponseEntity<>(NOT_FOUND);
        return new ResponseEntity<>(product, OK);
    }

    @RequestMapping(value = "/{code}", method = PUT)
    public ResponseEntity<Product> updateProduct(@PathVariable("code") String code, @RequestBody Product product) {
        Product currentProduct = (Product) salesResourcesService.findOne(Product.class, code);
        if (currentProduct == null) return new ResponseEntity<>(NOT_FOUND);
        currentProduct.setDescription(product.getDescription());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setInventoryBalance(product.getInventoryBalance());
        Product savedProduct = (Product) salesResourcesService.save(currentProduct);
        return new ResponseEntity<>(savedProduct, OK);
    }

    @RequestMapping(value = "/{code}", method = DELETE)
    public ResponseEntity<Product> deleteProduct(@PathVariable("code") String code) {
        Product product = (Product) salesResourcesService.findOne(Product.class, code);
        if (product == null) return new ResponseEntity<>(NOT_FOUND);
        salesResourcesService.delete(Product.class, code);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @RequestMapping(method = DELETE)
    public ResponseEntity<Product> deleteAllProducts() {
        salesResourcesService.deleteAll(Product.class);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
