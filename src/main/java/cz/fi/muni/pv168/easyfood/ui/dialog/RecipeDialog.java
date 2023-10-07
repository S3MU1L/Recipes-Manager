package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Recipe;

import javax.swing.*;

public final class RecipeDialog extends EntityDialog<Recipe> {

    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JTextField prepareTimeField = new JTextField();
    private final Recipe recipe;
    public RecipeDialog(Recipe recipe) {
        this.recipe = recipe;
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        caloriesField.setText(recipe.getFormattedCalories());
        prepareTimeField.setText(recipe.getFormattedPreparationTime());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories: ", caloriesField);
        add("Time to prepare (minutes): ", prepareTimeField);
    }

    @Override
    Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setPreparationTime(Integer.parseInt(prepareTimeField.getText()));
        return recipe;
    }
}
