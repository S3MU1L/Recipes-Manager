package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.lang.Math.round;

/**
 * @author Samuel Sabo
 */
public class ShowDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JTextField prepareTimeField = new JTextField();
    private final Recipe recipe;
    private final List<IngredientWithAmount> ingredients;
    private final JTextField categoriesField = new JTextField();
    private final Box ingredientsBox = Box.createVerticalBox();
    private final JScrollPane ingredientsField = new JScrollPane(ingredientsBox);

    public ShowDialog() {
        this(Recipe.createEmptyRecipe());
    }

    public ShowDialog(Object object) {
        this.recipe = (Recipe) object;
        ingredients = recipe.getIngredients();
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        caloriesField.setText(String.valueOf(round(recipe.getCalories())));
        prepareTimeField.setText(String.valueOf(recipe.getPreparationTime()));
        categoriesField.setText(recipe.getCategory().getName());

        Dimension dimension = new Dimension(150, 100);
        ingredientsField.setMaximumSize(dimension);

        for (IngredientWithAmount ingredient : ingredients) {
            ingredientsBox.add(new JCheckBox(ingredient.getName()));
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories (kJ): ", caloriesField);
        add("Time to prepare (minutes): ", prepareTimeField);
        add("Category:", categoriesField);
        add("Ingredients:", ingredientsField);
    }

    @Override
    public Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setPreparationTime(Utility.parseIntFromString(prepareTimeField.getText()));
        // only temporary solution, so that we can see calories we entered, will have to refactor this
        double calories = Utility.parseDoubleFromString(caloriesField.getText());
        recipe.addIngredient(new Ingredient("nahodna", calories, Unit.createEmptyUnit()), 1);
        return recipe;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ShowDialog();
    }

    @Override
    public EntityDialog<Recipe> createNewDialog(Recipe recipe, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ShowDialog(recipe);
    }
}
