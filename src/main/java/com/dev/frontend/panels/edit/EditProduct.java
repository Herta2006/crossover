package com.dev.frontend.panels.edit;

import com.dev.domain.Product;
import com.dev.frontend.services.Services;

import javax.swing.*;
import java.awt.*;

public class EditProduct extends EditContentPanel {
    private static final long serialVersionUID = -8971249970444644844L;
    private JTextField txtCode = new JTextField();
    private JTextField txtDescription = new JTextField();
    private JTextField txtQuantity = new JTextField();
    private JTextField txtPrice = new JTextField();

    public EditProduct() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Code"), gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtCode, gbc);
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        txtCode.setColumns(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Description"), gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        add(txtDescription, gbc);
        txtDescription.setColumns(28);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Price"), gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        add(txtPrice, gbc);
        txtPrice.setColumns(10);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 2;
        gbc.gridy = 2;
        add(new JLabel("Quantity"), gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        add(txtQuantity, gbc);
        txtQuantity.setColumns(10);
    }

    public boolean bindToGUI(Object o) {
        Product product = (Product) o;
        txtCode.setText(product.getId());
        txtDescription.setText(product.getDescription());
        txtPrice.setText("" + product.getPrice());
        txtQuantity.setText("" + product.getInventoryBalance());
        return false;
    }

    public Object guiToObject() {
        Product product = new Product();
        product.setId(txtCode.getText());
        product.setDescription(txtDescription.getText());
        product.setPrice(Long.parseLong(txtPrice.getText()));
        product.setInventoryBalance(Integer.parseInt(txtQuantity.getText()));
        return product;
    }

    public int getObjectType() {
        return Services.TYPE_PRODUCT;
    }

    @Override
    public String getCurrentCode() {
        return txtCode.getText();
    }

    public void clear() {
        txtCode.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
    }

    public void onInit() {

    }
}
