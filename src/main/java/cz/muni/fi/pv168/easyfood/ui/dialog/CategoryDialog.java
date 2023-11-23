package cz.muni.fi.pv168.easyfood.ui.dialog;


import cz.muni.fi.pv168.easyfood.business.model.Category;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryDialog extends EntityDialog<Category> implements ActionListener {
    private final JTextField nameField = new JTextField();
    private final JButton colorButton;
    private final Category category;
    private Color color;

    public CategoryDialog(Category category) {
        this.category = category;
        this.color = category.getColor();
        this.colorButton = new JButton("Choose Color");
        setValues();
        addFields();
    }

    public CategoryDialog() {
        this(Category.createEmptyCategory());
    }

    private void setValues() {
        nameField.setText(category.getName());
        colorButton.addActionListener(this);
        colorButton.setBackground(color);
    }

    private void addFields() {
        add("Name:", nameField);
        getPanel().add(colorButton, "width 100px, left");
    }

    @Override
    public Category getEntity() {
        String name = nameField.getText();
        return new Category(name, color);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        color = JColorChooser.showDialog(colorButton, "Choose", color);
        colorButton.setBackground(color);
    }
}
