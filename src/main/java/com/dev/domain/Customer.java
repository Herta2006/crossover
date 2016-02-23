package com.dev.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@XmlRootElement
@Entity
@Table(name = "CUSTOMERS")
public class Customer implements Serializable {
    @Id
    @Column(name = "ID", unique = true)
    private String id;

    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE1")
    private String phone1;

    @Column(name = "PHONE2")
    private String phone2;

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

    public String getId() {
        return id;
    }

    public void setCode(String id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
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
