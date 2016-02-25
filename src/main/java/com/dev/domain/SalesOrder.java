package com.dev.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.LazyCollectionOption.FALSE;

@Entity
@Table(name = "SALES_ORDERS")
public class SalesOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Column
//    @JoinTable(
//            name = "SALES_ORDERS_CUSTOMERS",
//            joinColumns = @JoinColumn(name = "SALES_ORDER_ID", foreignKey = @ForeignKey(name = "FK_SALES_ORDERS_CUSTOMERS_SO_ID")),
//            inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "FK_SALES_ORDERS_CUSTOMERS_C_ID"))
//    )
    private String customerId;

    @OneToMany(fetch = EAGER, cascade = {ALL})
    @LazyCollection(FALSE)
    @JoinTable(
            name = "SALES_ORDERS_LINES",
            joinColumns = @JoinColumn(name = "SALES_ORDER_ID", foreignKey = @ForeignKey(name = "FK_SALES_ORDERS_LINES_SO_ID")),
            inverseJoinColumns = @JoinColumn(name = "ORDER_LINE_ID", foreignKey = @ForeignKey(name = "FK_SALES_ORDERS_LINES_OL_ID"))
    )
    private List<OrderLine> orderLines;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}
