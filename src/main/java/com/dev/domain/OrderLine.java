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

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "PRODUCT_QUANTITY",
            joinColumns = @JoinColumn(name = "ORDER_LINE_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_QUANTITY_OL_ID")))
//    @MapKeyJoinColumn(name = "PRODUCT_ID", table = "PRODUCTS",  foreignKey = @ForeignKey(name = "FK_PRODUCT_QUANTITY_P_ID"))
    @MapKeyColumn(name = "PRODUCT_ID")
    @Column(name = "QUANTITY")
    private Map<String, Integer> productIdToQuantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Integer> getProductIdToQuantity() {
        return productIdToQuantity;
    }

    public void setProductIdToQuantity(Map<String, Integer> productIdToQuantity) {
        this.productIdToQuantity = productIdToQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderLine orderLine = (OrderLine) o;

        if (id != orderLine.id) return false;
        return productIdToQuantity != null ? productIdToQuantity.equals(orderLine.productIdToQuantity) : orderLine.productIdToQuantity == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (productIdToQuantity != null ? productIdToQuantity.hashCode() : 0);
        return result;
    }
}
