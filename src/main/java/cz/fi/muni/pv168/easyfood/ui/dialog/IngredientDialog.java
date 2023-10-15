package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Utility;

import javax.swing.*;


public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JComboBox<Unit> unitJComboBox = new JComboBox<>();
    private final Ingredient ingredient;


    public IngredientDialog(Ingredient ingredient) {
        this.ingredient = ingredient;
        setValues();
        addFields();
    }

    public IngredientDialog() {
        this(Ingredient.createEmptyIngredient());
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloriesField.setText(ingredient.getFormattedCalories());

        unitJComboBox.removeAllItems();
        for (Unit unit : Unit.values()) {
            unitJComboBox.addItem(unit);
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories: ", caloriesField);
        add("Unit ", unitJComboBox);
    }

    @Override
    Ingredient getEntity() {
        return new Ingredient(nameField.getText(), Utility.parseDoubleFromString(caloriesField.getText()), (Unit) unitJComboBox.getSelectedItem());
    }
}
