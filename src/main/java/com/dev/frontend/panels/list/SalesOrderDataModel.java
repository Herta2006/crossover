package com.dev.frontend.panels.list;

import com.dev.domain.SalesOrder;
import com.dev.frontend.services.Services;

import java.util.List;


public class SalesOrderDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747614655L;

    public SalesOrderDataModel() {
        super(new String[]{"Order Number", "Customer", "Total Price"}, 0);
    }

    @Override
    public int getObjectType() {
        return Services.TYPE_SALES_ORDER;
    }

    @Override
    public String[][] convertRecordsListToTableModel(List<Object> list) {
        return list.stream().map(object -> {
            SalesOrder salesOrder = (SalesOrder) object;
            String[] row = new String[3];
            row[0] = "" + salesOrder.getId();
            row[1] = "(" + salesOrder.getCustomerId() + ")" + Services.getCustomerById(salesOrder.getCustomerId());
            row[2] = "" + Services.calculateTotalPrice(salesOrder);
            return row;
        }).toArray(String[][]::new);
    }


}
