package com.dev.frontend.panels.list;

import com.dev.domain.Product;
import com.dev.frontend.services.Services;

import java.util.List;


public class ProductDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747614655L;

    public ProductDataModel() {
        super(new String[]{"Code", "Description", "Price", "Quantity"}, 0);
    }

    @Override
    public int getObjectType() {
        return Services.TYPE_PRODUCT;
    }

    @Override
    public String[][] convertRecordsListToTableModel(List<Object> list) {
        return list.stream().map(obj -> {
            Product product = (Product) obj;
            String[] record = new String[4];
            record[0] = product.getId();
            record[1] = product.getDescription();
            record[2] = "" + (product.getPrice() / 100.);
            record[3] = "" + product.getInventoryBalance();
            return record;
        }).toArray(String[][]::new);
    }
}
