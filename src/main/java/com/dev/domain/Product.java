package com.dev.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable {
    @Id
    @Column(name = "ID", unique = true)
    private String id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE_IN_EURO_CENTS")
    private Long price;

    @Column(name = "INVENTORY_BALANCE")
    private int inventoryBalance;

    public Product() {
    }

    public Product(String description, long price, int inventoryBalance) {
        this.description = description;
        this.price = price;
        this.inventoryBalance = inventoryBalance;
    }

    public Product(String id, String description, long price, int inventoryBalance) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.inventoryBalance = inventoryBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public int getInventoryBalance() {
        return inventoryBalance;
    }

    public void setInventoryBalance(int inventoryBalance) {
        this.inventoryBalance = inventoryBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id != null ? id.equals(product.id) : product.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

