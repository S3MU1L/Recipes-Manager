package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Samuel Sabo
 */
public class CategoryDialog extends EntityDialog<Category> implements ActionListener {
    private final JTextField nameField = new JTextField();
    private final JButton colorButton;
    private final Category category;
    private Color color = Color.WHITE;

    public CategoryDialog(Category category) {
        this.category = category;
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
    }

    private void addFields() {
        add("Name:", nameField);
        add("", colorButton);
    }

    @Override
    public Category getEntity() {
        return new Category(nameField.getText(), Color.WHITE);
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new CategoryDialog();
    }


    @Override
    public EntityDialog<Category> createNewDialog(Category entity, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new CategoryDialog(entity);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        color = JColorChooser.showDialog(colorButton, "Choose", color);
    }
}
