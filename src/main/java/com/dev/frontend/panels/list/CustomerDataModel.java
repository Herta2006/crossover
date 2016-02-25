package com.dev.frontend.panels.list;

import com.dev.domain.Customer;
import com.dev.frontend.services.Services;

import java.util.List;

public class CustomerDataModel extends ListDataModel {
    private static final long serialVersionUID = 7526529951747613655L;

    public CustomerDataModel() {
        super(new String[]{"Code", "Name", "Phone", "Current Credit"}, 0);
    }

    @Override
    public int getObjectType() {
        return Services.TYPE_CUSTOMER;
    }

    @Override
    public String[][] convertRecordsListToTableModel(List<Object> list) {
        String[][] sampleData = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            Customer customer = (Customer) list.get(i);
            sampleData[i] = convertFromCustomer(customer);
        }
        return sampleData;
    }

    private String[] convertFromCustomer(Customer customer) {
        String[] strings = new String[4];
        strings[0] = customer.getId();
        strings[1] = customer.getOrganizationName();
        strings[2] = customer.getPhone1();
        strings[3] = "" + (customer.getBalance() / 100.);
        return strings;
    }
}
