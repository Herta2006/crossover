package com.dev.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "CUSTOMERS")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

    @Column(name = "BALANCE_IN_EURO_CENTS")
    private long balance;

    @OneToMany(fetch = EAGER)
    @JoinTable(
            name = "CUSTOMERS_SALES_ORDERS",
            joinColumns = @JoinColumn(name = "CUSTOMER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMERS_SALES_ORDERS_C_ID")),
            inverseJoinColumns = @JoinColumn(name = "SALES_ORDER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CUSTOMERS_SALES_ORDERS_SO_ID"))
    )
    private List<SalesOrder> salesOrders;

    public Customer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<SalesOrder> getSalesOrders() {
        return salesOrders;
    }

    public void setSalesOrders(List<SalesOrder> salesOrders) {
        this.salesOrders = salesOrders;
    }
}
