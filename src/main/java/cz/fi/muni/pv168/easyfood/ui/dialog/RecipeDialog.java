package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Utility;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;
import static javax.swing.JOptionPane.*;

public final class  RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JTextField prepareTimeField = new JTextField();
    private final Recipe recipe;
    private final List<Ingredient> ingredients;
    private final JScrollPane categoriesField = new JScrollPane();
    private final Box ingredientsBox = Box.createVerticalBox();
    private final JScrollPane ingredientsField = new JScrollPane(ingredientsBox);

    public RecipeDialog(List<Ingredient> ingredients, List<Category> categories) {
        this(Recipe.createEmptyRecipe(), ingredients, categories);
    }

    public RecipeDialog(Recipe recipe, List<Ingredient> ingredients, List<Category> categories) {
        this.recipe = recipe;
        this.ingredients = ingredients;
        JList<String> categoriesList = new JList<>(categories.stream().map(Category::getName).toArray(String[]::new));
        categoriesField.setViewportView(categoriesList);
        setValues();
        addFields();
    }
    private void setValues() {
        nameField.setText(recipe.getName());
        caloriesField.setText(String.valueOf(round(recipe.getCalories())));
        prepareTimeField.setText(String.valueOf(recipe.getPreparationTime()));

        Dimension dimension = new Dimension(150, 100);
        categoriesField.setMaximumSize(dimension);
        ingredientsField.setMaximumSize(dimension);

        for (Ingredient ingredient : ingredients) {
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
        return new RecipeDialog(ingredients, categories);
    }

    @Override
    public EntityDialog<Recipe> createNewDialog(Recipe entity, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new RecipeDialog(entity, ingredients, categories);
    }

    @Override
    public Optional<Recipe> show(JComponent parentComponent, String title) {
        int result = JOptionPane.showOptionDialog(parentComponent, super.getPanel(), title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
        if (result == OK_OPTION) {
            new IngredientWithAmountDialog(recipe, ingredients).show(null, "Add amount to ingredients");
            return Optional.of(getEntity());
        } else {
            return Optional.empty();
        }
    }
}
