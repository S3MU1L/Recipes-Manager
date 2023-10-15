package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;

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
    public Category getEntity() {
        category.setName(nameField.getText());
        return category;
    }

    @Override
    public EntityDialog<Category> createNewDialog(Object entity) {
        return new CategoryDialog((Category) entity);
    }

    @Override
    public EntityDialog<Category> createNewDialog() {
        return createNewDialog(Category.createEmptyCategory());
    }
}
