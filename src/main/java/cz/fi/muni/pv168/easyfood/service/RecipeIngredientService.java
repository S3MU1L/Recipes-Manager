package cz.fi.muni.pv168.easyfood.service;

import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.windows.WindowsManager;

import java.util.Collections;
import java.util.List;

public class RecipeIngredientService implements Service<IngredientAndAmount> {

    private final Recipe recipe;
    private final WindowsManager windowsManager;

    public RecipeIngredientService(Recipe recipe, WindowsManager windowsManager) {
        this.recipe = recipe;
        this.windowsManager = windowsManager;
    }

    @Override
    public void add(IngredientAndAmount entity) {
        recipe.addIngredient(entity.getIngredient(), entity.getAmount());
        windowsManager.notifyAddedRecipeIngredient(recipe);
    }

    @Override
    public void update(IngredientAndAmount entity) {
        recipe.updateIngredient(entity.getIngredient(), entity.getAmount());
    }

    @Override
    public void delete(IngredientAndAmount entity) {
        int i = recipe.deleteIngredient(entity.getIngredient());
        windowsManager.notifyDeletedRecipeIngredient(recipe, entity.getIngredient(), i);
    }

    @Override
    public List<IngredientAndAmount> getEntityList() {
        return Collections.unmodifiableList(recipe.getIngredients());
    }

    @Override
    public void openAddWindow() {
        throw new UnsupportedOperationException("Adding IngredientAndAmount via window is not supported");
    }

    @Override
    public void openUpdateWindow(IngredientAndAmount entity) {
        throw new UnsupportedOperationException("Updating IngredientAndAmount via window is not supported");
    }

    @Override
    public void openShowWindow(IngredientAndAmount entity) {
        throw new UnsupportedOperationException("Showing IngredientAndAmount via window is not supported");
    }
}
