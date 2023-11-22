package cz.fi.muni.pv168.easyfood.service;

import cz.fi.muni.pv168.easyfood.data.RecipeDao;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.windows.WindowsManager;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeService extends BaseService<Recipe> {
    private final WindowsManager windowsManager;
    private final RecipeDao recipeDao;

    public RecipeService(RecipeDao recipeDao, WindowsManager windowsManager) {
        super(recipeDao);
        this.windowsManager = windowsManager;
        this.recipeDao = recipeDao;
    }

    @Override
    public void add(Recipe recipe) {
        new AddRecipeWorker(recipe).execute();
    }

    @Override
    public void update(Recipe recipe) {
        new UpdateRecipeWorker(recipe).execute();
    }

    @Override
    public void delete(Recipe recipe) {
        new DeleteRecipeWorker(recipe).execute();
    }

    @Override
    public void openAddWindow() {
        windowsManager.openAddRecipeWindow();
    }

    @Override
    public void openUpdateWindow(Recipe recipe) {
        windowsManager.openEditRecipeWindow(recipe);
    }

    @Override
    public void openShowWindow(Recipe recipe) {
        windowsManager.openRecipeWindow(recipe);
    }

    @Override
    public List<Recipe> getEntityList() {
        return Collections.unmodifiableList(entityList);
    }

    @Override
    public void updateAll() {
        windowsManager.notifyUpdateAll();
    }

    public List<Long> getContainingRecipeIDs(Ingredient ingredient) {
        return recipeDao.getContainingIDs(ingredient);
    }

    public void generateShoppingList(List<Recipe> selectedEntities) {
        var amountMap = sumAmounts(selectedEntities);
        var ingredientAndAmountList = new ArrayList<IngredientAndAmount>();
        for (var ingredient : amountMap.keySet()) {
            ingredientAndAmountList.add(new IngredientAndAmount(ingredient, amountMap.get(ingredient)));
        }
        windowsManager.showShoppingList(ingredientAndAmountList);
    }

    private HashMap<Ingredient, Double> sumAmounts(List<Recipe> selectedEntities) {
        var amountMap = new HashMap<Ingredient, Double>();
        for (var recipe : selectedEntities) {
            for (var ingredient : recipe.getIngredients()) {
                amountMap.putIfAbsent(ingredient.getIngredient(), 0D);
                amountMap.put(ingredient.getIngredient(), amountMap.get(ingredient.getIngredient()) + ingredient.getAmount());
            }
        }
        return amountMap;
    }


    private class AddRecipeWorker extends SwingWorker<Void, Void> {

        private final Recipe recipe;

        public AddRecipeWorker(Recipe recipe) {
            this.recipe = recipe;
        }

        @Override
        protected Void doInBackground() {
            return dataAccessObject.create(recipe);
        }

        @Override
        protected void done() {
            try {
                get();
                entityList.add(recipe);
                windowsManager.notifyAddedRecipe();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), "Error when adding recipe",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

    private class UpdateRecipeWorker extends SwingWorker<Void, Void> {

        private final Recipe recipe;

        public UpdateRecipeWorker(Recipe recipe) {
            this.recipe = recipe;
        }

        @Override
        protected Void doInBackground() {
            return dataAccessObject.update(recipe);
        }

        @Override
        protected void done() {
            try {
                get();
                int index = entityList.indexOf(recipe);
                Recipe updatedRecipe = entityList.get(index);
                updatedRecipe.setTo(recipe); // We keep just one instance in memory!
                windowsManager.notifyUpdatedRecipe(updatedRecipe, index);
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), "Error when updating recipe",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

    private class DeleteRecipeWorker extends SwingWorker<Void, Void> {

        private final Recipe recipe;

        public DeleteRecipeWorker(Recipe recipe) {
            this.recipe = recipe;
        }

        @Override
        protected Void doInBackground() {
            return dataAccessObject.delete(recipe);
        }

        @Override
        protected void done() {
            try {
                get();
                int index = entityList.indexOf(recipe);
                entityList.remove(index);
                windowsManager.notifyDeletedRecipe(recipe, index);
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), "Error when removing recipe",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

}
