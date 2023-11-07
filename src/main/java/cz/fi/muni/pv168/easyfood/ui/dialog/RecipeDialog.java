package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Utility;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final JTextField prepareTimeField = new JTextField();
    private final Recipe recipe;
    private final List<Ingredient> ingredients;
    private final JScrollPane categoriesField = new JScrollPane();
    private final Box ingredientsBox = Box.createVerticalBox();
    private final List<JTextField> ingredientAmounts = new ArrayList<>();
    private final JScrollPane ingredientsField = new JScrollPane(ingredientsBox);

    public RecipeDialog(List<Ingredient> ingredients, List<Category> categories) {
        this(Recipe.createEmptyRecipe(), ingredients, categories);
    }

    public RecipeDialog(Recipe recipe, List<Ingredient> ingredients, List<Category> categories) {
        this.recipe = recipe;
        this.ingredients = ingredients;
        JList<String> categoriesList = new JList<>(categories.stream().map(Category::getName).toArray(String[]::new));
        if (recipe.getCategory() != null) {
            categoriesList.setSelectedValue(recipe.getCategory().getName(), true);
        }
        categoriesField.setViewportView(categoriesList);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        prepareTimeField.setText(String.valueOf(recipe.getPreparationTime()));

        Dimension dimension = new Dimension(250, 100);
        categoriesField.setMaximumSize(dimension);
        ingredientsField.setMaximumSize(dimension);

        for (Ingredient ingredient : ingredients) {
            Box box = Box.createHorizontalBox();
            var lst = recipe.getIngredients().stream()
                    .filter(ingredient1 -> ingredient1.getName().equals(ingredient.getName()))
                    .map(IngredientWithAmount::getAmount)
                    .toList();

            double amount = lst.size() > 0 ? lst.get(0) : 0;
            JTextField jTextField = new JTextField(String.valueOf(amount));

            ingredientAmounts.add(jTextField);
            var label = new JLabel(ingredient.getName() + " (" + ingredient.getUnit().getAbbreviation() + "): ");
            box.add(label);
            box.add(jTextField, "wmin 50lp, grow");

            ingredientsBox.add(box);
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Time to prepare (minutes): ", prepareTimeField);
        add("Category:", categoriesField);
        add("Ingredients:", ingredientsField);
    }

    @Override
    public Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setPreparationTime(Utility.parseIntFromString(prepareTimeField.getText()));
        recipe.setIngredients(new ArrayList<>());
        for (Ingredient ingredient : ingredients) {
            double amount = Double.parseDouble(ingredientAmounts.get(ingredients.indexOf(ingredient)).getText());
            if (amount == 0) {
                continue;
            }
            recipe.addIngredient(ingredient, amount);
        }
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
}
