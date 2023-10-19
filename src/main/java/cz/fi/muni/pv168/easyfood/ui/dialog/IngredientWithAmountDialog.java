package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.*;
import java.util.List;

/**
 * @author Samuel Sabo
 */
public class IngredientWithAmountDialog extends EntityDialog<Recipe> {
    private final Recipe recipe;
    private final List<Ingredient> ingredients;
    private final JTextField[] ingredientsAmount;

    public IngredientWithAmountDialog(Recipe recipe, List<Ingredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
        ingredientsAmount = new JTextField[ingredients.size()];
        setValues();
        addFields();
    }

    private void setValues() {
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsAmount[i] = new JTextField();
        }
    }

    private void addFields() {
        for (int i = 0; i < ingredients.size(); i++) {
            add(ingredients.get(i).getName(), ingredientsAmount[i]);
        }
    }

    @Override
    public Recipe getEntity() {
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
