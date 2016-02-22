package com.dev.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "ORDER_LINES")
public class OrderLine implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "PRODUCT_QUANTITY",
            joinColumns = @JoinColumn(name = "ORDER_LINE_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_QUANTITY_OL_ID")))
    @MapKeyJoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_QUANTITY_P_ID"))
    @Column(name = "QUANTITY")
    private Map<Product, Integer> productQuantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<Product, Integer> getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Map<Product, Integer> productQuantity) {
        this.productQuantity = productQuantity;
    }
}
