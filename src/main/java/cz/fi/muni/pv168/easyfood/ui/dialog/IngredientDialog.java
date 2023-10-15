package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.ComboBoxModelAdapter;

import javax.swing.*;

/**
 * @author Samuel Sabo
 */
public class IngredientDialog extends EntityDialog<Ingredient>{
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final ComboBoxModel<Unit> unitModel;
    private final Ingredient ingredient;
    public IngredientDialog(Ingredient ingredient, ListModel<Unit> unitModel) {
        this.ingredient = ingredient;
        this.unitModel = new ComboBoxModelAdapter<>(unitModel);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloriesField.setText(ingredient.getFormattedCalories());
        unitModel.setSelectedItem(ingredient.getUnit());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories (kJ): ", caloriesField);
        add("Measured in: ", new JComboBox<>(unitModel));
    }

    @Override
    Ingredient getEntity() {
        ingredient.setName(nameField.getText());
        ingredient.setCalories(Integer.parseInt(caloriesField.getText()));
        ingredient.setUnit((Unit) unitModel.getSelectedItem());
        return ingredient;
    }
}
