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
        String[][] data = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            data[i] = covertFromSalesOrder((SalesOrder) list.get(i));
        }
        return data;
    }

    private String[] covertFromSalesOrder(SalesOrder salesOrder) {
        String[] strings = new String[3];
        strings[0] = "" + salesOrder.getId();
        strings[1] = "(" + salesOrder.getCustomerId() + ")" + "Customer's Organization Name";
        strings[2] = "122.5";
        return strings;
    }
}
