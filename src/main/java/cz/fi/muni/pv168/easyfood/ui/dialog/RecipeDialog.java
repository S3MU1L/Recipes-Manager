package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Utility;

import javax.swing.*;

public final class  RecipeDialog extends EntityDialog<Recipe> {

    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JTextField prepareTimeField = new JTextField();
    private final Recipe recipe;

    public RecipeDialog(Recipe recipe) {
        this.recipe = recipe;
        setValues();
        addFields();
    }

    public RecipeDialog() {
        this(Recipe.createEmptyRecipe());
    }
    private void setValues() {
        nameField.setText(recipe.getName());
        caloriesField.setText(recipe.getFormattedCalories());
        prepareTimeField.setText(recipe.getFormattedPreparationTime());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories (kJ): ", caloriesField);
        add("Time to prepare (minutes): ", prepareTimeField);
    }

    @Override
    public Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setPreparationTime(Utility.parseIntFromString(prepareTimeField.getText()));
        // only temporary solution, so that we can see calories we entered, will have to refactor this
        double calories = Utility.parseDoubleFromString(caloriesField.getText());
        recipe.addIngredient(new Ingredient("nahodna", calories, Unit.GRAM), 1);
        return recipe;
    }

    @Override
    public EntityDialog<Recipe> createNewDialog() {
        return new RecipeDialog();
    }

}
