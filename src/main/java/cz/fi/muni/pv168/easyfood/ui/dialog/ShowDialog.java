package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

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

    public ShowDialog(Recipe recipe) {
        this.recipe = recipe;
        ingredients = recipe.getIngredients();
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        nameField.setEditable(false);
        caloriesField.setText(String.valueOf(round(recipe.getCalories())));
        caloriesField.setEditable(false);
        prepareTimeField.setText(String.valueOf(recipe.getPreparationTime()));
        prepareTimeField.setEditable(false);
        categoriesField.setText(recipe.getCategory().getName());
        categoriesField.setEditable(false);

        Dimension dimension = new Dimension(150, 100);
        ingredientsField.setMaximumSize(dimension);

        for (IngredientWithAmount ingredient : ingredients) {
            JTextField jTextField = new JTextField(ingredient.getName());
            jTextField.setEditable(false);
            ingredientsBox.add(jTextField);
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

    @Override
    public Optional<Recipe> show(JComponent parentComponent, String title) {
        JOptionPane.showMessageDialog(parentComponent, super.getPanel(), "Show", INFORMATION_MESSAGE, null);
        return Optional.ofNullable(recipe);
    }
}
