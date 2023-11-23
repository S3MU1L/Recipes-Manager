package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Filter;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.MainWindow;
import cz.fi.muni.pv168.easyfood.ui.dialog.FilterDialog;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class FilterAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final TabContainer filterContainer;
    private final List<Recipe> originalRecipes;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    private final MainWindow mainWindow;

    public FilterAction(MainWindow mainWindow, TabContainer tabContainer, TabContainer filterContainer, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        super("Filter", Icons.FILTER_ICON);
        this.mainWindow = mainWindow;
        this.tabContainer = tabContainer;
        this.filterContainer = filterContainer;
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        originalRecipes = new ArrayList<>(this.recipes);
        putValue(SHORT_DESCRIPTION, "Filter recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog =
                (FilterDialog) filterContainer.getSelectedTab().getDialog().createNewDialog(Filter.createEmptyFilter(), ingredients, categories, units);
        Filter filter = dialog.show(filterContainer.getComponent(), "Filter").orElse(null);
        JTable recipeTable = tabContainer.getSelectedTab().getTable();
        RecipeTableModel recipeTableModel = (RecipeTableModel) recipeTable.getModel();

        if (filter != null && filter.getName().equals("") && filter.getCategories().size() == 0 && filter.getIngredients().size() == 0 &&
                filter.getPreparationTime() == 0 && filter.getMinimumNutritionalValue() == 0 &&
                filter.getMaximumNutritionalValue() == 0 && filter.getPortions() == 0) {
            recipeTableModel.clear();
            originalRecipes.forEach(recipeTableModel::addRow);
        } else if (filter != null) {
            List<Recipe> filteredRecipes = filter.getFilteredRecipes(recipes);
            recipeTableModel.clear();
            filteredRecipes.forEach(recipeTableModel::addRow);
        }
        mainWindow.updateRecipeCountLabel();
    }
}
