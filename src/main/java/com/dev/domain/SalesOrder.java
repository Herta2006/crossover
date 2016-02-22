package com.dev.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "SALES_ORDERS")
public class SalesOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @OneToMany(fetch = EAGER)
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

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}