package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.IngredientService;
import cz.fi.muni.pv168.easyfood.service.RecipeService;
import cz.fi.muni.pv168.easyfood.ui.I18N;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowsManager {

    private IngredientService ingredientService;
    private RecipeService recipeService;

    private MainWindow mainWindow;
    private static final Map<Long, RecipeWindow> recipeWindows = new HashMap<>();

    private static final List<AbstractWindow> windows = new ArrayList<>();

    public void setIngredientService(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void openWindow(MainWindow mainWindow) {
        if (this.mainWindow != null) {
            throw new IllegalArgumentException("Trying to open another main window");
        }
        this.mainWindow = mainWindow;
        this.mainWindow.showWindow();
        windows.add(mainWindow);
    }

    public JFrame getParentFrame() {
        return mainWindow.getFrame();
    }

    public void openWindow(AddRecipeWindow addRecipeWindow) {
        addRecipeWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windows.remove(addRecipeWindow);
                super.windowClosing(e);
            }
        });
        addRecipeWindow.showWindow();
        windows.add(addRecipeWindow);
    }

    public static void openWindow(AbstractWindow window) {
        window.showWindow();
        windows.add(window);
    }

    public void openAddIngredientWindow() {
        new AddIngredientWindow(ingredientService, getParentFrame()).showWindow();
    }

    public void openAddRecipeWindow() {
        openWindow(new AddRecipeWindow(recipeService, ingredientService, getParentFrame(),
                Recipe.getEmptyRecipe(), this));
    }

    public void openEditRecipeWindow(Recipe recipe) {
        openWindow(new AddRecipeWindow(recipeService, ingredientService, getParentFrame(), recipe, this));
    }

    public void openRecipeWindow(Recipe recipe) {
        RecipeWindow recipeWindow = new RecipeWindow(recipe, getParentFrame(), this);
        Long ID = recipe.getID();
        RecipeWindow window = recipeWindows.get(ID);
        if (window == null) {
            window = recipeWindow;
            recipeWindows.put(ID, window);
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    recipeWindows.remove(ID, recipeWindow);
                    windows.remove(recipeWindow);
                    super.windowClosing(e);
                }
            });
        }
        window.showWindow();
        windows.add(recipeWindow);
    }

    public void openIngredientErrorWindow() {
        openWindow(new InfoWindow(
                new I18N(IngredientService.class).getString("deleteUsedIngredients"),
                mainWindow.getFrame())
        );
    }

    public void notifyAddedIngredient() {
        for (AbstractWindow window : windows) {
            window.onAddedIngredient();
        }
    }

    public void notifyAddedRecipe() {
        for (AbstractWindow window : windows) {
            window.onAddedRecipe();
        }
    }

    public void notifyAddedRecipeIngredient(Recipe recipe) {
        for (AbstractWindow window : windows) {
            window.onAddedRecipeIngredient(recipe);
        }
    }

    public void notifyDeletedIngredient(Ingredient ingredient, int index) {
        for (AbstractWindow window : windows) {
            window.onDeletedIngredient(ingredient, index );
        }
    }

    public void notifyDeletedRecipe(Recipe recipe, int index) {
        for (AbstractWindow window : windows) {
            window.onDeletedRecipe(recipe, index);
        }
    }

    public void notifyDeletedRecipeIngredient(Recipe recipe, Ingredient ingredient, int index) {
        for (AbstractWindow window : windows) {
            window.onDeletedRecipeIngredient(recipe, ingredient, index);
        }
    }

    public void notifyUpdatedRecipe(Recipe recipe, int index) {
        for (AbstractWindow window : windows) {
            window.onUpdatedRecipe(recipe, index);
        }
    }

    public void notifyUpdateAll() {
        for (AbstractWindow window : windows) {
            window.onUpdateAll();
        }
    }

    public void showShoppingList(ArrayList<IngredientAndAmount> ingredientAndAmountList) {
        openWindow(new ShoppingListWindow(ingredientAndAmountList, mainWindow.getFrame()));
    }
}
