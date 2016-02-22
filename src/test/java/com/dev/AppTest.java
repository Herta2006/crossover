package com.dev;

import com.dev.domain.Customer;
import com.dev.domain.OrderLine;
import com.dev.domain.Product;
import com.dev.domain.SalesOrder;
import com.dev.repositories.CustomerRepository;
import com.dev.repositories.OrderLinesRepository;
import com.dev.repositories.ProductRepository;
import com.dev.repositories.SalesOrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AppTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderLinesRepository orderLinesRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;
    private Customer expectedCustomer;

    @Test
    public void findCustomer() {
        //todo:
        // Hibernate as a persistence provider has a bug:
        // findOne returns duplicates of SalesOrder
        // The same problem is https://jira.spring.io/browse/DATAJPA-709
        List<Customer> customers = customerRepository.findAll();
        assertTrue(customers.size() == 1);
        Customer actual = customers.get(0);
        assertEquals(expectedCustomer.getOrganizationName(), actual.getOrganizationName());
        assertEquals(1000_00L, actual.getBalance());
        assertEquals(2, actual.getSalesOrders().size());

        assertEquals(2, actual.getSalesOrders().get(0).getOrderLines().size());
        Map<Product, Integer> productsQuantity = actual.getSalesOrders().get(0).getOrderLines().get(0).getProductQuantity();
        assertEquals(3, productsQuantity.size());
        // Assertion for product's fields as well because, they used by map -> in hash code and in equals methods
        for (Product product : productsQuantity.keySet()) {
            assertEquals(new Integer(product.getTitle().replace("Product", "")), productsQuantity.get(product));
        }

        productsQuantity = actual.getSalesOrders().get(0).getOrderLines().get(1).getProductQuantity();
        assertEquals(2, productsQuantity.size());
        // Assertion for product's fields as well because, they used by map -> in hash code and in equals methods
        for (Product product : productsQuantity.keySet()) {
            assertEquals(new Integer(product.getTitle().replace("Product", "")), productsQuantity.get(product));
        }

        assertEquals(1, actual.getSalesOrders().get(1).getOrderLines().size());
        productsQuantity = actual.getSalesOrders().get(1).getOrderLines().get(0).getProductQuantity();
        // Assertion for product's fields as well because, they used by map -> in hash code and in equals methods
        assertEquals(1, productsQuantity.size());
        for (Product product : productsQuantity.keySet()) {
            assertEquals(new Integer(product.getTitle().replace("Product", "")), productsQuantity.get(product));
        }
    }

    @Test
    public void updateCustomer() {
        expectedCustomer.setBalance(2000_00);
        expectedCustomer.setOrganizationName("Updated Organization Name");
//        Map<Product, Integer> productQuantity = expectedCustomer.getSalesOrders().get(0).getOrderLines().get(0).getProductQuantity();
//        Product product = productQuantity.keySet().iterator().next();
//        product.setTitle("Updated Product Title");
//        product.setPrice(1000_00L);
//        product.setInventoryBalance(2);
//        productQuantity.put(product, 10);
        assertTrue(2000_00 == expectedCustomer.getBalance());
        assertEquals("Updated Organization Name", expectedCustomer.getOrganizationName());
//        productQuantity = savedCustomer.getSalesOrders().get(0).getOrderLines().get(0).getProductQuantity();
//        product = productQuantity.keySet().iterator().next();
//        assertEquals("Updated Product Title", product.getTitle());
//        assertTrue(1000_00L == product.getPrice());
//        assertTrue(2 == product.getInventoryBalance());
    }

    @Test
    public void deleteCustomer() {
        customerRepository.delete(1L);
        List<Customer> customers = customerRepository.findAll();
        assertTrue(customers.isEmpty());
    }

    @Before
    public void prepareDatabase() {
        Product product1 = new Product("Product1", 10L, 1);
        productRepository.save(product1);
        Product product2 = new Product("Product2", 20L, 2);
        productRepository.save(product2);
        Product product3 = new Product("Product3", 30L, 3);
        productRepository.save(product3);
        Product product4 = new Product("Product4", 40L, 4);
        productRepository.save(product4);
        Product product5 = new Product("Product5", 50L, 5);
        productRepository.save(product5);
        Product product6 = new Product("Product6", 60L, 6);
        productRepository.save(product6);

        OrderLine orderLine1 = new OrderLine();
        Map<Product, Integer> productsQuantity = new HashMap<>();
        productsQuantity.put(product1, 1);
        productsQuantity.put(product2, 2);
        productsQuantity.put(product3, 3);
        orderLine1.setProductQuantity(productsQuantity);
        orderLinesRepository.save(orderLine1);

        OrderLine orderLine2 = new OrderLine();
        productsQuantity = new HashMap<>();
        productsQuantity.put(product4, 4);
        productsQuantity.put(product5, 5);
        orderLine2.setProductQuantity(productsQuantity);
        orderLinesRepository.save(orderLine2);

        OrderLine orderLine3 = new OrderLine();
        productsQuantity = new HashMap<>();
        productsQuantity.put(product6, 6);
        orderLine3.setProductQuantity(productsQuantity);
        orderLinesRepository.save(orderLine3);

        SalesOrder salesOrder1 = new SalesOrder();
        salesOrder1.setOrderLines(new ArrayList<>(asList(orderLine1, orderLine2)));
        salesOrderRepository.save(salesOrder1);
        SalesOrder salesOrder2 = new SalesOrder();
        salesOrder2.setOrderLines(singletonList(orderLine3));
        salesOrderRepository.save(salesOrder2);

        expectedCustomer = new Customer("Crossover Inc");
        expectedCustomer.setBalance(1000_00L); //1000 euro
        expectedCustomer.setSalesOrders(new ArrayList<>(asList(salesOrder1, salesOrder2)));
        Customer savedCustomer = customerRepository.saveAndFlush(expectedCustomer);
    }

    @After
    public void clearDatabase() {
        salesOrderRepository.deleteAll();
        orderLinesRepository.deleteAll();
        productRepository.deleteAll();
    }
}
