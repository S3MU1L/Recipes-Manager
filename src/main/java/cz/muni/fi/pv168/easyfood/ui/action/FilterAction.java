package cz.muni.fi.pv168.easyfood.ui.action;


import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Filter;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.dialog.FilterDialog;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class FilterAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final TabContainer filterContainer;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    private final MainWindow mainWindow;

    public FilterAction(MainWindow mainWindow,
                        TabContainer tabContainer,
                        TabContainer filterContainer,
                        List<Recipe> recipes,
                        List<Ingredient> ingredients,
                        List<Category> categories,
                        List<Unit> units) {
        super("Filter", Icons.FILTER_ICON);
        this.mainWindow = mainWindow;
        this.tabContainer = tabContainer;
        this.filterContainer = filterContainer;
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        putValue(SHORT_DESCRIPTION, "Filter recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog =
                (FilterDialog) filterContainer.getSelectedTab().getDialog().createNewDialog(Filter.createEmptyFilter(), recipes, ingredients, categories, units);

        JTable recipeTable = tabContainer.getSelectedTab().getTable();
        RecipeTableModel recipeTableModel = (RecipeTableModel) recipeTable.getModel();

        if(!recipeTableModel.isActiveFiter()){
            dialog.resetFilter();
        }

        Filter filter = dialog.show(filterContainer.getComponent(), "Filter").orElse(null);
        if (filter != null && filter.getName().isEmpty() &&
                filter.getCategories().isEmpty() &&
                filter.getIngredients().isEmpty() &&
                filter.getPreparationTime() == 0 &&
                filter.getMinimumNutritionalValue() == 0 &&
                filter.getMaximumNutritionalValue() == 0 &&
                filter.getMaxPortion() == 0 &&
                filter.getMinPortion() == 0)
        {
            recipeTableModel.reset();
        } else if (filter != null) {
            recipeTableModel.updateWithFilter(filter);
            mainWindow.updateFilterStatus();
        }

        mainWindow.updateRecipeCountLabel();
    }
}
