package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.ComboBoxModelAdapter;

import javax.swing.*;

/**
 * @author Samuel Sabo
 */
public class CategoryDialog extends EntityDialog<Category {
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final ComboBoxModel<Color> colorModel;
    private final Category category;
    public CategoryDialog(Category ingredient, ListModel<Unit> unitModel) {
        this.category = ingredient;
        this.unitModel = new ComboBoxModelAdapter<>(unitModel);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(category.getName());
        caloriesField.setText(category.getFormattedCalories());
        unitModel.setSelectedItem(category.getUnit());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories (kJ): ", caloriesField);
        add("Measured in: ", new JComboBox<>(unitModel));
    }

    @Override
    Category getEntity() {
        category.setName(nameField.getText());
        category.setCalories(Integer.parseInt(caloriesField.getText()));
        category.setUnit((Unit) unitModel.getSelectedItem());
        return category;
    }
}
