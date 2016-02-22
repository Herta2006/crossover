package com.dev.domain;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true)
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PRICE_IN_EURO_CENTS")
    private Long price;

    @Column(name = "INVENTORY_BALANCE")
    private int inventoryBalance;

    public Product() {
    }

    public Product(String title, long price, int inventoryBalance) {
        this.title = title;
        this.price = price;
        this.inventoryBalance = inventoryBalance;
    }

    public Product(long id, String title, long price, int inventoryBalance) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.inventoryBalance = inventoryBalance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

        return id == product.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

