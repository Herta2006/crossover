package com.dev.frontend.panels.list;

import com.dev.domain.SalesOrder;
import com.dev.frontend.services.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SalesOrderDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747614655L;

    public SalesOrderDataModel() {
        super(new String[]{"Order Number", "Customer", "Total Price"}, 0);
    }

    private final static Map<String, String[]> tableData = new HashMap<>();

    @Override
    public int getObjectType() {
        return Services.TYPE_SALES_ORDER;
    }

    @Override
    public String[][] convertRecordsListToTableModel(List<Object> list) {
        tableData.clear();
        list.stream().forEach(object -> {
            SalesOrder salesOrder = (SalesOrder) object;
            if (tableData.containsKey(salesOrder.getCustomerId())) {
                String[] row = tableData.get(salesOrder.getCustomerId());
                row[2] = "" + (Double.parseDouble(row[2]) + Services.calculateTotalPrice(salesOrder));
            } else {
                String[] row = new String[3];
                row[0] = "" + salesOrder.getId();
                row[1] = "(" + salesOrder.getCustomerId() + ")" + Services.getCustomerById(salesOrder.getCustomerId());
                row[2] = "" + Services.calculateTotalPrice(salesOrder);
                tableData.put(salesOrder.getCustomerId(), row);
            }
        });
        return tableData.values().toArray(new String[tableData.values().size()][]);
    }


}
