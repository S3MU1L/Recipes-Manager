package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class CategoryDialog extends EntityDialog<Category> implements ActionListener {
    private final JTextField nameField = new JTextField();
    private final JButton colorButton;
    private final Category category;
    private final List<Category> categories;
    private Color color;

    public CategoryDialog(Category category, List<Category> categories) {
        this.category = category;
        this.categories = categories;
        this.colorButton = new JButton("Choose Color");
        setValues();
        addFields();
    }

    public CategoryDialog(List<Category> categories) {
        this(Category.createEmptyCategory(), categories);
    }

    private void setValues() {
        color = category.getColor();
        nameField.setText(category.getName());
        colorButton.addActionListener(this);
        colorButton.setBackground(color);
    }

    private void addFields() {
        add("*Name:", nameField);
        getPanel().add(colorButton, "width 100px, left");
    }

    @Override
    public Category getEntity() {
        category.setName(nameField.getText().trim());
        category.setColor(color);
        return category;
    }

    @Override
    public boolean valid(Category category) {
        StringBuilder stringBuilder = new StringBuilder();

        if (category.getName().trim().equals("")) {
            stringBuilder.append("Please enter a valid name\n\n");
        }
        if (!categories.stream().filter(category1 -> !category1.getGuid().equals(category.getGuid()) && category1.getName().equals(category.getName())).toList()
                       .isEmpty()) {
            stringBuilder.append("Duplicate name: ").append(category.getName()).append("\n\n");
        }

        if (stringBuilder.isEmpty()){
            return true;
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Category Dialog Error", ERROR_MESSAGE, null);
        return false;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients,
                                           List<Category> categories, List<Unit> units) {
        return new CategoryDialog(categories);
    }

    @Override
    public EntityDialog<Category> createNewDialog(Category entity, List<Recipe> recipes, List<Ingredient> ingredients,
                                                  List<Category> categories, List<Unit> units) {
        return new CategoryDialog(entity, categories);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        color = JColorChooser.showDialog(colorButton, "Choose", color);
        colorButton.setBackground(color);
    }
}
