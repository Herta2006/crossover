package com.dev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable {
    @JsonProperty("id")
    @Id
    @Column(name = "ID", unique = true)
    private String id;

    @JsonProperty("description")
    @Column(name = "DESCRIPTION")
    private String description;

    @JsonProperty("price")
    @Column(name = "PRICE_IN_EURO_CENTS")
    private long price;

    @JsonProperty("inventoryBalance")
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", inventoryBalance=" + inventoryBalance +
                '}';
    }
}

