package com.dev;

import com.dev.domain.Customer;
import com.dev.domain.OrderLine;
import com.dev.domain.Product;
import com.dev.domain.SalesOrder;
import com.dev.server.repositories.CustomerRepository;
import com.dev.server.repositories.OrderLinesRepository;
import com.dev.server.repositories.ProductRepository;
import com.dev.server.repositories.SalesOrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContextConfig.class)
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
    private Customer preparedCustomer;
    private Product preparedProduct;
//    private SalesOrder preparedSalesOrder;

    @Test
    public void findCustomer() {
        Customer actual = customerRepository.findOne(preparedCustomer.getId());
        assertEquals(preparedCustomer.getOrganizationName(), actual.getOrganizationName());
        assertEquals(preparedCustomer.getAddress(), actual.getAddress());
        assertEquals(preparedCustomer.getPhone1(), actual.getPhone1());
        assertEquals(preparedCustomer.getPhone2(), actual.getPhone2());
        assertEquals(preparedCustomer.getBalance(), actual.getBalance());
    }

    @Test
    public void updateCustomer() {
        Customer customer = customerRepository.findOne(preparedCustomer.getId());
        customer.setOrganizationName("Updated Organization Name");
        customer.setAddress("Updated Address");
        customer.setPhone1("123000");
        customer.setPhone2("321000");
        customer.setBalance(2000_00);
        customerRepository.saveAndFlush(customer);
        Customer actual = customerRepository.findOne(preparedCustomer.getId());
        assertEquals(customer.getOrganizationName(), actual.getOrganizationName());
        assertEquals(customer.getAddress(), actual.getAddress());
        assertEquals(customer.getPhone1(), actual.getPhone1());
        assertEquals(customer.getPhone2(), actual.getPhone2());
        assertEquals(customer.getBalance(), actual.getBalance());
    }

    @Test
    public void deleteCustomer() {
        customerRepository.delete(preparedCustomer.getId());
        List<Customer> customers = customerRepository.findAll();
        assertTrue(customers.isEmpty());
    }

    @Test
    public void findProduct() {
        Product actual = productRepository.findOne(preparedProduct.getId());
        assertEquals(preparedProduct.getDescription(), actual.getDescription());
        assertTrue(preparedProduct.getPrice() == actual.getPrice());
        assertTrue(preparedProduct.getInventoryBalance() == actual.getInventoryBalance());
    }

    @Test
    public void updateProduct() {
        Product product = productRepository.findOne(preparedProduct.getId());
        product.setDescription("Updated Description");
        product.setPrice(200L);
        product.setInventoryBalance(20);
        productRepository.saveAndFlush(product);
        Product actual = productRepository.findOne(preparedProduct.getId());
        assertEquals(actual.getDescription(), actual.getDescription());
        assertTrue(actual.getPrice() == actual.getPrice());
        assertTrue(actual.getInventoryBalance() == actual.getInventoryBalance());
    }

    @Test
    public void deleteProduct() {
        productRepository.delete(preparedProduct.getId());
        List<Product> customers = productRepository.findAll();
        assertTrue(customers.isEmpty());
    }

    @Before
    public void prepareDatabase() {
        preparedProduct = new Product();
        preparedProduct.setId("1P");
        preparedProduct.setDescription("Description");
        preparedProduct.setPrice(100L);
        preparedProduct.setInventoryBalance(10);
        Product savedProduct = productRepository.saveAndFlush(preparedProduct);

        preparedCustomer = new Customer();
        preparedCustomer.setCode("1C");
        preparedCustomer.setOrganizationName("Organization Name");
        preparedCustomer.setAddress("Address");
        preparedCustomer.setPhone1("123");
        preparedCustomer.setPhone2("321");
        preparedCustomer.setBalance(1000_00L); //1000 euro
        Customer savedCustomer = customerRepository.saveAndFlush(preparedCustomer);

//        OrderLine orderLine = new OrderLine();
//        Map<Product, Integer> productsQuantity = new HashMap<>();
//        productsQuantity.put(preparedProduct, 1);
//        orderLine.setProductQuantity(productsQuantity);
//        orderLinesRepository.save(orderLine);
//        preparedSalesOrder = new SalesOrder();
//        preparedSalesOrder.setCustomer(preparedCustomer);
//        preparedSalesOrder.setOrderLines(singletonList(orderLine));
//        SalesOrder savedSalesOrder = salesOrderRepository.save(preparedSalesOrder);
    }

    @After
    public void clearDatabase() {
        salesOrderRepository.deleteAll();
        orderLinesRepository.deleteAll();
        productRepository.deleteAll();
    }
}
