package cz.fi.muni.pv168.easyfood.service;

import cz.fi.muni.pv168.easyfood.data.IngredientDao;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.windows.WindowsManager;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class IngredientService extends BaseService<Ingredient> {

    private final WindowsManager windowsManager;
    private final RecipeService recipeService;
    private final IngredientDao ingredientDao;
    private final List<Ingredient> ingredientList;

    public IngredientService(IngredientDao ingredientDao, WindowsManager windowsManager, RecipeService recipeService) {
        super(ingredientDao);
        this.ingredientDao = ingredientDao;
        this.windowsManager = windowsManager;
        this.recipeService = recipeService;
        // We want to have just one instance for every ingredient,
        // so reuse already loaded ingredients from recipeService
        this.ingredientList = extractIngredients(recipeService.getEntityList());
        for (Ingredient ingredient : ingredientDao.findAll()) {
            if (!ingredientList.contains(ingredient)) {
                ingredientList.add(ingredient);
            }
        }
    }

    private List<Ingredient> extractIngredients(List<Recipe> recipes) {
        Set<Ingredient> ingredients = new HashSet<>();
        recipes.forEach(recipe -> ingredients.addAll(
                recipe.getIngredients().stream()
                        .map(IngredientAndAmount::getIngredient)
                        .collect(Collectors.toSet())));
        return new ArrayList<>(ingredients);
    }

    @Override
    public void add(Ingredient ingredient) {
        new AddIngredientWorker(ingredient).execute();
    }

    @Override
    public void update(Ingredient ingredient) {
        throw new UnsupportedOperationException("Functionality Not Implemented Yet");
    }

    @Override
    public void delete(Ingredient ingredient) {
        new DeleteIngredientWorker(ingredient).execute();
    }

    @Override
    public void openAddWindow() {
        windowsManager.openAddIngredientWindow();
    }

    @Override
    public void openUpdateWindow(Ingredient ingredient) {
        throw new UnsupportedOperationException("Functionality Not Implemented Yet");
    }

    @Override
    public void openShowWindow(Ingredient ingredient) {
        throw new UnsupportedOperationException("Functionality Not Implemented Yet");
    }

    @Override
    public List<Ingredient> getEntityList() {
        return Collections.unmodifiableList(ingredientList);
    }

    private boolean inAnyRecipe(Ingredient ingredient) {
        return recipeService.getContainingRecipeIDs(ingredient).size() > 0;
    }

    private class AddIngredientWorker extends SwingWorker<Void, Void> {

        private final Ingredient ingredient;

        public AddIngredientWorker(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        protected Void doInBackground() {
            return dataAccessObject.create(ingredient);
        }

        @Override
        protected void done() {
            try {
                get();
                ingredientList.add(ingredient);
                windowsManager.notifyAddedIngredient();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), "Error when adding ingredient",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

    private class DeleteIngredientWorker extends SwingWorker<Boolean, Void> {

        private final Ingredient ingredient;

        public DeleteIngredientWorker(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        protected Boolean doInBackground() {
            if (!inAnyRecipe(ingredient)) {
                ingredientDao.delete(ingredient);
                return true;
            }
            return false;
        }

        @Override
        protected void done() {
            try {
                if (get()) {
                    int index = ingredientList.indexOf(ingredient);
                    ingredientList.remove(index);
                    windowsManager.notifyDeletedIngredient(ingredient, index);
                } else {
                    windowsManager.openIngredientErrorWindow();
                }
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), "Error when removing ingredients",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }
}
