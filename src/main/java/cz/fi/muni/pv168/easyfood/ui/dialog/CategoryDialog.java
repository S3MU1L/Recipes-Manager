package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Recipe;

import javax.swing.*;

public class CategoryDialog extends EntityDialog<Category> {
    private final JTextField nameField = new JTextField();
    private final Category category;

    public CategoryDialog(Category category) {
        this.category = category;
        setValues();
        addFields();
    }

    public CategoryDialog() {
        this(Category.createEmptyCategory());
    }

    private void setValues() {
        nameField.setText(category.getName());
    }

    private void addFields() {
        add("Name:", nameField);
    }

    @Override
    Category getEntity() {
        return new Category(nameField.getText());
    }
}