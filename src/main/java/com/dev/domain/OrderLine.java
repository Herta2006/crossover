package com.dev.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "ORDER_LINES")
public class OrderLine implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Column(name = "PRODUCT_ID")
    private String productId;
    @Column(name = "QUANTITY")
    private Integer quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderLine orderLine = (OrderLine) o;

        return productId != null ? productId.equals(orderLine.productId) : orderLine.productId == null;

    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }
}
