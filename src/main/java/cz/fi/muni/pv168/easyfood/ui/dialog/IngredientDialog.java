package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JScrollPane unitsField = new JScrollPane();
    private final Ingredient ingredient;

    public IngredientDialog(List<Unit> units) {
        this(Ingredient.createEmptyIngredient(), units);
    }

    public IngredientDialog(Ingredient ingredient, List<Unit> units) {
        this.ingredient = ingredient;
        JList<String> unitsList = new JList<>(units.stream().map(Unit::getName).toArray(String[]::new));
        unitsField.setViewportView(unitsList);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloriesField.setText(String.valueOf(ingredient.getCalories()));

        Dimension dimension = new Dimension(150, 100);
        unitsField.setMaximumSize(dimension);
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories (kJ): ", caloriesField);
        add("Unit: ", unitsField);
    }

    @Override
    public Ingredient getEntity() {
        //return new Ingredient(nameField.getText(), Utility.parseDoubleFromString(caloriesField.getText()), (Unit) unitsField.getSelectedItem());
        return Ingredient.createEmptyIngredient();
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new IngredientDialog(units);
    }


    @Override
    public EntityDialog<Ingredient> createNewDialog(Ingredient entity, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new IngredientDialog(entity, units);
    }
}
